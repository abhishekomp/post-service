package org.aom.post_service.post;

import org.aom.post_service.post.exception.PostNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class PostServiceUnitTest {

    private PostService postService;
    private PostRepository postRepository;

    @BeforeEach
    void setUp(){
        postRepository = Mockito.mock(PostRepository.class);
        postService = new PostService(postRepository);
    }

    @Test
    public void getAllPostsByUserId() {
        //given
        Post post1 = new Post("USER1", "post1", "post body1", LocalDate.of(2024, 9, 1), false);
        Post post2 = new Post("USER2", "post2", "post body2", LocalDate.of(2024, 9, 10), false);
        Post post3 = new Post("USER1", "post3", "post body3", LocalDate.of(2024, 8, 1), true);
        given(postRepository.findAll()).willReturn(List.of(post1, post2, post3));

        //when
        List<Post> posts = postService.getAllPostsByUserId("USER1");

        //then
        assertThat(posts.size()).isEqualTo(2);
        assertThat(posts).hasSize(2)
                .extracting(Post::getTitle)
                .containsExactlyInAnyOrder("post1", "post3");
    }

    @Test
    public void getSinglePostShouldThrowExceptionWhenPostDoesNotExistInDb() {
        //given
        int postId = 99999;
        given(postRepository.findById(postId)).willThrow(new PostNotFoundException(String.format("Post with id %s not found", postId)));

        //when
        //postService.findById(postId);

        //then
        assertThatThrownBy(() -> postService.findById(postId))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessage(String.format("Post with id %s not found", postId));

        //exact number of invocations verification
        verify(postRepository, times(1)).findById(postId);
    }
}