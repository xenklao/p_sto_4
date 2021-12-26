package com.javamentor.qa.platform.webapp.controllers.rest;

import com.javamentor.qa.platform.models.dto.question.TagDTO;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.TagDTOService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "TagResourceController", description = "Позволяет работать с тегами")
@RestController
public class TagResourceController {

    private TagDTOService tagDTOServiceImpl;

    @Autowired
    public void setTagServiceImpl(TagDTOService tagDTOServiceImpl) {
        this.tagDTOServiceImpl = tagDTOServiceImpl;
    }

    @Operation(summary = "Получение списка пользовательских тегов",
            description = "Получение списка тегов текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список TagDTO (id, name, persist_date)",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))
                    }),
            @ApiResponse(responseCode = "403", description = "Доступ запрещён")
    })
    @GetMapping(path = "/api/user/tag/tracked")
    public @ResponseBody List<TagDTO> getTagDto(Authentication authentication) {
        User currentUser = (User)authentication.getPrincipal();
        return tagDTOServiceImpl.getTagsDTOByUserIdFromTrackedTag(currentUser.getId());
    }
}
