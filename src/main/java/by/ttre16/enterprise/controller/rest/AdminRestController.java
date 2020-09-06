package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.controller.AbstractUserController;
import by.ttre16.enterprise.dto.to.UserTo;
import by.ttre16.enterprise.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.util.web.UrlUtil.ADMIN_REST_URL;

@RestController
@RequestMapping(ADMIN_REST_URL)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminRestController extends AbstractUserController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserTo> getAllUsers() {
        return super.getAll().stream()
                .map(user -> userMapper.toDto(user))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        super.delete(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody UserTo userTo,
            @PathVariable Integer id) {
        User userToUpdate = super.get(id);
        userMapper.updateEntityFromDto(userToUpdate, userTo);
        super.save(userToUpdate, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserTo create(@Valid @RequestBody UserTo userTo) {
        User createdUser = super.createAdmin(userMapper.toEntity(userTo));
        return userMapper.toDto(createdUser);
    }
}
