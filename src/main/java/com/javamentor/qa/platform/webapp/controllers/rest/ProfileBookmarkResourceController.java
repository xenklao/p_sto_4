package com.javamentor.qa.platform.webapp.controllers.rest;


import com.javamentor.qa.platform.dao.impl.pagination.bookmarks.BookMarkPageDtoDaoImpl;
import com.javamentor.qa.platform.models.dto.BookMarksDto;
import com.javamentor.qa.platform.models.dto.GroupBookmarkDto;
import com.javamentor.qa.platform.models.dto.PageDTO;
import com.javamentor.qa.platform.models.entity.GroupBookmark;
import com.javamentor.qa.platform.models.dto.UserProfileGroup;
import com.javamentor.qa.platform.models.entity.bookmark.BookMarks;
import com.javamentor.qa.platform.models.entity.bookmark.SortBookmark;
import com.javamentor.qa.platform.models.entity.pagination.PaginationData;
import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.dto.BookMarksDtoService;
import com.javamentor.qa.platform.service.abstracts.model.BookmarksService;
import com.javamentor.qa.platform.service.abstracts.model.GroupBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


import javax.validation.Valid;
import java.util.List;
import java.util.Optional;



@RestController
@Tag(name = "ProfileBookmarkResourceController", description = "Позволяет работать с закладками пользователя")
@RequestMapping("/api/user/profile")
public class ProfileBookmarkResourceController {

    private final BookMarksDtoService bookMarksDtoService;
    private final GroupBookmarkService groupBookmarkService;
    private final BookmarksService bookmarksService;


    public ProfileBookmarkResourceController(BookMarksDtoService bookMarksDtoService, GroupBookmarkService groupBookmarkService, BookmarksService bookmarksService) {
        this.bookMarksDtoService = bookMarksDtoService;
        this.groupBookmarkService = groupBookmarkService;
        this.bookmarksService = bookmarksService;

    }


    @Operation(summary = "Получение пагинированного списка всех закладок в профиле пользователя в виде BookMarksDto" +
                         "Есть два необязательных параметра. " +
                         "Первый :sortBookmark - сортирует закладки по дате (sortBookmark=NEW), " +
                         "по просмотрам (sortBookmark=VIEW), по голосам (sortBookmark=VIEW). " +
                         "" +
                         "Второй: groupId, если параметр не передан, то возвращаются все закладки," +
                         "если передан Long groupId - возвращает только те закладки, которые относятся " +
                         "к указанной группе",

            description = "Получение закладок в профиле пользователя в виде BookMarksDto")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список List<BookMarksDto> ( bookmarkId, groupBookmarkId, " +
                                  "groupBookmarkTitle ,questionId, title, listTagDto, countAnswer, countVote, " +
                                  "countView, persistDateTime, note)",
                    content = {
                            @Content(
                                    mediaType = "application/json")
                    }),
    })
    @GetMapping("/bookmarks")
    public ResponseEntity<PageDTO<BookMarksDto>> getAllBookMarksInUserProfile(@AuthenticationPrincipal User user,
                                                                              @RequestParam(required = false,
                                                                                            defaultValue = "NEW",
                                                                                            name = "sortBookmark"
                                                                              ) SortBookmark sortBookmark,
                                                                              @RequestParam(required = false,
                                                                                            defaultValue = "1"
                                                                              ) int page,
                                                                              @RequestParam(defaultValue = "10"
                                                                              ) int items,
                                                                              @RequestParam(required = false
                                                                              ) Long groupId) {

        PaginationData data = new PaginationData(page, items, BookMarkPageDtoDaoImpl.class.getSimpleName());
        data.getProps().put("user", user);
        data.getProps().put("userId", user.getId());
        data.getProps().put("sortBookmark", sortBookmark);
        data.getProps().put("groupId", groupId);

        return new ResponseEntity<>(bookMarksDtoService.getPageDto(data), HttpStatus.OK);
    }

    @Operation(summary = "Получение списка названий групп пользователя",
            description = "Возвращает список имен(title) GroupBookMark")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Возвращает список имен(title) GroupBookMark",
                    content = {
                            @Content(
                                    mediaType = "application/json"
                            )
                    }
            )
    })
    @GetMapping("/bookmark/group")
    public ResponseEntity<List<UserProfileGroup>> getAllUserBookMarkGroupNames(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(groupBookmarkService.getAllUserBookMarkGroupNamesByUserId(user.getId()), HttpStatus.OK);
    }

    @Operation(summary = "Создание новой группы закладок")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Создание новой группы закладок",
                            content = {
                                    @Content(
                                            mediaType = "application/json"
                                    )
                            })
            }
    )
    @PostMapping("/bookmark/group")
    public ResponseEntity addNewGroupBookMark(@AuthenticationPrincipal User user, @RequestBody(required = false) String title) {
        if (title == null || title.isEmpty()) {
            return new ResponseEntity<>("request body (title field) must not be empty", HttpStatus.BAD_REQUEST);
        }
        if (groupBookmarkService.isGroupBookMarkExistsByName(user.getId(), title)) {
            return new ResponseEntity<>("user already has group bookmark with title " + title, HttpStatus.BAD_REQUEST);
        }
        GroupBookmark groupBookmark = GroupBookmark.builder()
                .user(user)
                .title(title)
                .build();

        groupBookmarkService.persist(groupBookmark);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление закладки авторизированного пользователя по questionId")
    @ApiResponse(responseCode = "200",
            description = "Bookmark удален",
            content = {
                    @Content(
                            mediaType = "application/json"
                    )
            })
    @ApiResponse(responseCode = "400",
            description = "Bookmark с таким questionId не существует",
            content = {
                    @Content(
                            mediaType = "application/json"
                    )
            })
    @DeleteMapping("/bookmark/{id}")
    public ResponseEntity<?> deleteBookmarkByQuestionId(@PathVariable("id") @RequestBody Long questionId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<BookMarks> bookmarks = bookmarksService.getBookmarkByQuestionIdAndUserId(user.getId(), questionId);
        if (bookmarks.isPresent()) {
            bookmarksService.deleteById(questionId);
            return new ResponseEntity<>("Закладка с id = " + questionId + " была успешно удалена", HttpStatus.OK);
        }
        return new ResponseEntity<>("Закладка с id = " + questionId + " не существует", HttpStatus.BAD_REQUEST);
    }



    @Operation(
            summary = "Добавление примечания к закладке",
            description = "Добавление примечания к закладке"
    )
    @ApiResponse(responseCode = "200", description = "Примечание добавлено", content = {
            @Content(mediaType = "application/json")
    })
    @ApiResponse(responseCode = "400", description = "Примечание не добавлено", content = {
            @Content(mediaType = "application/json")
    })
    @PostMapping("/bookmark/{bookmarkId}/note")
    public ResponseEntity<?> addNoteToBookmarkInProfile(@PathVariable("bookmarkId") Long bookmarkId,
                                                        @AuthenticationPrincipal User user,
                                                        @RequestBody(required = false) String note) {

        Optional<BookMarks> bookMarks = bookmarksService.getById(bookmarkId);
        if (bookMarks.isPresent() && user.getId().equals(bookMarks.get().getUser().getId())) {
            BookMarks bm = bookMarks.get();
            bm.setNote(note);
            bookmarksService.update(bm);
            return new ResponseEntity<>("Note is added", HttpStatus.CREATED);
        }

        return new ResponseEntity<>("Bookmark is not found", HttpStatus.BAD_REQUEST);




    }

    @Operation(summary = "Изменение названия группы Bookmark")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Изменение названия группы Bookmark",
                            content = {
                                    @Content(
                                            mediaType = "application/json"
                                    )
                            })
            }
    )


    @PutMapping("/{bookmarkId}/group")
    public ResponseEntity<?> changeGroupBookmarkName(@PathVariable("bookmarkId") long bookmarkId, @Valid @RequestBody GroupBookmarkDto GroupBookmarkDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        GroupBookmark groupBookmark = GroupBookmark.builder()
                .user(user)
                .title(GroupBookmarkDto.getTitle())
                .id(GroupBookmarkDto.getId())
                .build();
        groupBookmarkService.update(groupBookmark);
        return new ResponseEntity<>(HttpStatus.OK);


    }


}