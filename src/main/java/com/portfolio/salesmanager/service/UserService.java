package com.portfolio.salesmanager.service;


import com.portfolio.salesmanager.dto.request.ChangePasswordRequest;
import com.portfolio.salesmanager.dto.request.UpdateUserProfileRequest;
import com.portfolio.salesmanager.dto.request.UpdateUserRequest;
import com.portfolio.salesmanager.dto.request.UserRequest;
import com.portfolio.salesmanager.dto.response.UserResponse;
import com.portfolio.salesmanager.entity.Branch;
import com.portfolio.salesmanager.entity.User;
import com.portfolio.salesmanager.exception.BranchNotFoundException;
import com.portfolio.salesmanager.exception.InvalidPasswordException;
import com.portfolio.salesmanager.exception.UserAlreadyExistsException;
import com.portfolio.salesmanager.exception.UserNotFoundException;
import com.portfolio.salesmanager.mapper.UserMapper;
import com.portfolio.salesmanager.repository.BranchRepository;
import com.portfolio.salesmanager.repository.UserRepository;
import com.portfolio.salesmanager.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll(){

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse findUser(Long idUser){

        return userRepository.findById(idUser)
                .map(userMapper::toResponse)
                .orElseThrow(()-> new UserNotFoundException("Usuario no encontrado"));
    }

    public List<UserResponse> findUsersByRole(Role role){

        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> findActiveUsers(){

        return userRepository.findByActiveTrue()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<UserResponse> findUsersByBranch(Long idBranch){

        branchRepository.findById(idBranch)
                .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));

        return userRepository.findByBranch_IdBranch(idBranch)
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponse saveUser(UserRequest userRequest){

        if (userRepository.existsByUsername(userRequest.getUsername())){
            throw new UserAlreadyExistsException("El username ya existe");
        }
        Branch branch= null;

        if (userRequest.getBranchId() != null) {
            branch = branchRepository.findById(userRequest.getBranchId())
                    .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));
        }

        if ((userRequest.getRole() == Role.SELLER || userRequest.getRole() == Role.SUPERVISOR) && branch == null) {
            throw new BranchNotFoundException("Un vendedor o supervisor debe estar asociado a una sucursal");
        }

        if (userRequest.getRole() == Role.ADMIN || userRequest.getRole() == Role.CUSTOMER) {
            branch = null;
        }

        User user= User.builder()
                .name(userRequest.getName())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .active(userRequest.getActive() != null ? userRequest.getActive() : true)
                .branch(branch)
                .build();

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateMyProfile(UpdateUserProfileRequest updateUserProfileRequest){

        User user= getAuthenticatedUser();

        user.setName(updateUserProfileRequest.getName());
        
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUserAdmin(Long idUser, UpdateUserRequest updateUserRequest){

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        Branch branch= user.getBranch();

        if (updateUserRequest.getBranchId() != null) {
            branch = branchRepository.findById(updateUserRequest.getBranchId())
                    .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));
        }

        Role finalRole= updateUserRequest.getRole() !=null
                ?updateUserRequest.getRole()
                :user.getRole();

        if ((finalRole == Role.SELLER || finalRole == Role.SUPERVISOR) && branch == null) {
            throw new BranchNotFoundException("Un vendedor o supervisor debe estar asociado a una sucursal");
        }

        if (finalRole == Role.ADMIN || finalRole == Role.CUSTOMER) {
            branch = null;
        }


        if (updateUserRequest.getName() != null) {
            user.setName(updateUserRequest.getName());
        }

        if (updateUserRequest.getRole() != null) {
            user.setRole(updateUserRequest.getRole());
        }

        if (updateUserRequest.getActive() != null) {
            user.setActive(updateUserRequest.getActive());
        }

        user.setBranch(branch);

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long idUser){

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        user.setActive(false);

        userRepository.save(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest){

        User user= getAuthenticatedUser();

        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), user.getPassword())){

            throw new InvalidPasswordException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);
    }



    public User getAuthenticatedUser(){

        String username= SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(()-> new UserNotFoundException("Usuario no encontrado"));
    }

}
