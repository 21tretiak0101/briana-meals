package by.ttre16.enterprise.controller.rest;

import by.ttre16.enterprise.annotation.Authenticated;
import by.ttre16.enterprise.controller.AbstractMealController;
import by.ttre16.enterprise.dto.to.MealTo;
import by.ttre16.enterprise.model.AuthenticatedUser;
import by.ttre16.enterprise.model.Meal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static by.ttre16.enterprise.util.web.UrlUtil.MEAL_REST_URL;
import static org.springframework.web.servlet.support
        .ServletUriComponentsBuilder.fromCurrentContextPath;

@RestController
@RequestMapping(MEAL_REST_URL)
public class MealRestController extends AbstractMealController {
    @GetMapping
    public List<MealTo> getAll(@Authenticated AuthenticatedUser user) {
        return super.getAll(user.getId()).stream()
                .map(meal -> mealMapper.toDto(meal))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MealTo> create(@Valid @RequestBody MealTo mealTo,
            @Authenticated AuthenticatedUser user) {
        Meal created = super.create(mealMapper.toEntity(mealTo), user.getId());
        URI newResourceUri = fromCurrentContextPath()
                .path(MEAL_REST_URL + "{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity
                .created(newResourceUri)
                .body(mealMapper.toDto(created));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody MealTo mealTo,
            @PathVariable Integer id, @Authenticated AuthenticatedUser user) {
        Meal mealToUpdate = super.get(user.getId(), id);
        mealMapper.updateEntityFromDto(mealToUpdate, mealTo);
        super.save(user.getId(), mealToUpdate, id);
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id,
            @Authenticated AuthenticatedUser user) {
        super.delete(user.getId(), id);
    }

    @GetMapping("/filter")
    public List<MealTo> getWithExcess(@RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate, @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime,
            @Authenticated AuthenticatedUser user) {
        return super.getBetween(
                startDate,
                endDate,
                startTime,
                endTime,
                user.getId()
        );
    }
}
