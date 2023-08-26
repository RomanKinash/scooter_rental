package com.example.scooter_rental.controller;

import com.example.scooter_rental.dto.request.ScooterRequestDto;
import com.example.scooter_rental.dto.response.ScooterResponseDto;
import com.example.scooter_rental.mapper.DtoMapper;
import com.example.scooter_rental.model.Scooter;
import com.example.scooter_rental.service.ScooterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scooters")
@AllArgsConstructor
@Tag(name = "Scooter", description = "The Scooter API. "
        + "Describes the complete set of operations that can be performed on scooter.")
public class ScooterController {
    private final ScooterService scooterService;
    private final DtoMapper<ScooterRequestDto, ScooterResponseDto, Scooter> scooterMapper;

    @Operation(summary = "Add new scooter",
            description = "This method is responsible for managing the addition of a new scooter. "
                    + "It expects a request DTO (Data Transfer Object) containing the scooter's details "
                    + "as input and adds this scooter to the system via the scooterService. Subsequently,"
                    + " the method transforms the resulting Scooter object into a response DTO (ScooterResponseDto)"
                    + " using the scooterMapper and returns it.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Scooter.class),
                            mediaType = "application/json")})
    })
    @PostMapping
    public ScooterResponseDto add(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Here you need to fill in the fields to create a new scooter")
                                  @RequestBody ScooterRequestDto requestDto) {
        Scooter scooter = scooterService.add(scooterMapper.mapToModel(requestDto));
        return scooterMapper.mapToDto(scooter);
    }

    @Operation(summary = "Get all scooters",
            description = "This method is responsible for fetching all the scooters stored in the system. "
                    + "It achieves this by invoking the scooterService to obtain a list of Scooter objects. "
                    + "Subsequently, for each Scooter object, it applies the scooterMapper to generate the corresponding"
                    + " response DTO (ScooterResponseDto). Lastly, it aggregates all these mapped DTOs into a List "
                    + "and delivers it as the result.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Scooter.class),
                            mediaType = "application/json")})
    })
    @GetMapping
    public List<ScooterResponseDto> getAll() {
        return scooterService.getAll().stream()
                .map(scooterMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get scooter by id",
            description = "This method is responsible for fetching a particular scooter based on its "
                    + "unique identifier (id). It accepts the id as a path variable and uses the scooterService"
                    + " to retrieve the corresponding Scooter object from the system. Subsequently, it converts this "
                    + "Scooter object into the corresponding response DTO (ScooterResponseDto) using the scooterMapper and "
                    + "returns it as the output.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Scooter.class),
                            mediaType = "application/json")})
    })
    @GetMapping("/{id}")
    public ScooterResponseDto get(@Parameter(
            description = "Press the button \"Try it out\" and put id to get the scooter")
                                  @PathVariable Long id) {
        return scooterMapper.mapToDto(scooterService.get(id));
    }

    @Operation(summary = "Update scooter by id",
            description = "The method updates of an existing scooter record. It "
                    + "takes the scooter's unique identifier (id) as a path variable "
                    + "and the updated scooter details as a request body in the form "
                    + "of a validated ScooterRequestDto. The method then maps the "
                    + "requestDto to a Scooter object using the scooterMapper and calls "
                    + "the scooterService to update the corresponding scooter record in the "
                    + "system. Finally, it maps the updated Scooter object to its "
                    + "corresponding response DTO (ScooterResponseDto) using the "
                    + "scooterMapper and returns it.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Scooter.class),
                            mediaType = "application/json")})
    })
    @PutMapping("/{id}")
    public ScooterResponseDto update(@Parameter(
            description = "Press the button \"Try it out\" and put id to update the scooter")
                                     @PathVariable Long id,
                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                         description = "Here you need to specify "
                                                 + "the fields that need to be "
                                                 + "updated in json format")
                                 @RequestBody @Valid ScooterRequestDto requestDto) {
        Scooter scooter = scooterMapper.mapToModel(requestDto);
        return scooterMapper.mapToDto(scooterService.update(id, scooter));
    }

    @Operation(summary = "Delete scooter by id",
            description = "The method deletes of a specific scooter record. It takes the"
                    + " scooter's unique identifier (id) as a path variable and calls the "
                    + "scooterService to delete the corresponding scooter from the system. The "
                    + "method does not return any data, as it is meant to perform the "
                    + "deletion operation without providing a response body.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Scooter.class),
                            mediaType = "application/json")})
    })
    @DeleteMapping("/{id}")
    public void delete(@Parameter(
            description = "Press the button \"Try it out\" and put id to delete the scooter")
                           @PathVariable Long id) {
        scooterService.delete(id);
    }
}
