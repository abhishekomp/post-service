package org.aom.post_service.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.aom.post_service.post.exception.ApiError;
import org.aom.post_service.post.exception.PostNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PostController.class})
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private PostController postController;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @BeforeEach
    void setUp(){
        postController = new PostController(postService);
    }

    @Test
    void shouldThrowPostNotFoundException() throws Exception {
        when(postService.findById(1010)).thenThrow(new PostNotFoundException("Post with id 1010 not found"));
        mockMvc.perform(get("/post-api/post/{id}", 1010))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(PostNotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("Post with id 1010 not found"));
    }

    @Test
    void shouldThrowValidationException() throws Exception {
        //when(postService.findById(1010)).thenThrow(new PostNotFoundException("Post with id 1010 not found"));
        MvcResult mvcResult = mockMvc.perform(get("/post-api/post/{id}", "AA"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(HandlerMethodValidationException.class))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);
        ApiError apiError = objectMapper.readValue(contentAsString, ApiError.class);
        assertThat(apiError.getErrorMsg()).isEqualTo("id should be all numbers");
        assertThat(apiError.getErrCode()).isEqualTo(400);

        ApiError expectedApiErr = new ApiError(LocalDateTime.now(), "id should be all numbers", 400);
        assertThat(apiError).usingRecursiveComparison().ignoringFields("errorDateTime").isEqualTo(expectedApiErr);
    }

    @Test
    void shouldThrowPostNotFoundExceptionWhenPostToDeleteIsNotAvailable() throws Exception {
        //when(postService.deletePost(1010)).thenThrow(new PostNotFoundException("Post with id 1010 not found"));
        doThrow(new PostNotFoundException("Post with id 1010 not found")).when(postService).deletePost(1010);
        mockMvc.perform(delete("/post-api/post/{id}", "1010"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(PostNotFoundException.class))
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("Post with id 1010 not found"));
    }

    @Test
    void shouldReturnOkWhenPostToDeleteIsAvailable() throws Exception {
        //doThrow(new PostNotFoundException("Post with id 1010 not found")).when(postService).deletePost(1010);
        doNothing().when(postService).deletePost(anyInt());
        mockMvc.perform(delete("/post-api/post/{id}", "1010"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        //andExpect(jsonPath("$").doesNotExist())
        //verify(postService).deletePost(any());
        verify(postService, times(1)).deletePost(anyInt());
    }

    @Test
    void shouldThrowValidationExceptionWhenIdIsNotIntegerForDeleteEndPoint() throws Exception {
        //when(postService.findById(1010)).thenThrow(new PostNotFoundException("Post with id 1010 not found"));
        MvcResult mvcResult = mockMvc.perform(delete("/post-api/post/{id}", "101A"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(HandlerMethodValidationException.class))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);
        ApiError apiError = objectMapper.readValue(contentAsString, ApiError.class);
        assertThat(apiError.getErrorMsg()).isEqualTo("id should be all numbers");
        assertThat(apiError.getErrCode()).isEqualTo(400);

        ApiError expectedApiErr = new ApiError(LocalDateTime.now(), "id should be all numbers", 400);
        assertThat(apiError).usingRecursiveComparison().ignoringFields("errorDateTime").isEqualTo(expectedApiErr);
    }

    @Test
    void shouldReturnTheNewPostObjectWhenNewPostRecordIsCreatedUsingHTTPPostEndPoint() throws Exception {
        Post expectedPostObj = new Post(1,"USER3", "New Post1", "post body1", LocalDate.of(2022, 9, 1), false);
        when(postService.createNewPost(any(Post.class))).thenReturn(expectedPostObj);

        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        Post requestPostObj = new Post("USER3", "New Post1", "post body1", LocalDate.of(2022, 9, 1), false);
        String requestJson=ow.writeValueAsString(requestPostObj);
        System.out.println(requestJson);

        MvcResult result = mockMvc.perform(post("/post-api/createPost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        System.out.println("Response json = " + json);
        Post post = objectMapper.readValue(json, Post.class);
        assertThat(post.getId()).isEqualTo(1);
        assertThat(post).usingRecursiveComparison().ignoringFields("id").isEqualTo(requestPostObj);
    }
}