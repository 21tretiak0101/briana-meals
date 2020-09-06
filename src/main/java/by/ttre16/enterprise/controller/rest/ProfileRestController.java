package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.annotation.Authenticated;
import by.ttre16.enterprise.controller.AbstractUserController;
import by.ttre16.enterprise.dto.to.UserTo;
import by.ttre16.enterprise.model.AuthenticatedUser;
import by.ttre16.enterprise.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static by.ttre16.enterprise.util.web.UrlUtil.PROFILE_REST_URL;

@RestController
@RequestMapping(PROFILE_REST_URL)
public class ProfileRestController extends AbstractUserController {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserTo get(@Authenticated AuthenticatedUser user) {
        return userMapper.toDto(super.get(user.getId()));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserTo userTo,
        @Authenticated AuthenticatedUser user) {
        User userToUpdate = super.get(user.getId());
        userMapper.updateEntityFromDto(userToUpdate, userTo);
        super.save(userToUpdate, user.getId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Authenticated AuthenticatedUser user) {
        super.delete(user.getId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserTo create(@Valid @RequestBody UserTo userTo) {
        User createdUser = super.create(userMapper.toEntity(userTo));
        return userMapper.toDto(createdUser);
    }
}
