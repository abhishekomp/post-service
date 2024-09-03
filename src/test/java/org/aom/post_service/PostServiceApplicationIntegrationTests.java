package org.aom.post_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.aom.post_service.post.Post;
import org.aom.post_service.post.exception.PostNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class PostServiceApplicationIntegrationTests {
	@Autowired
	private MockMvc mockMvc;

	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void shouldGetAllPosts() throws Exception {
		MvcResult result = mockMvc.perform(get("/post-api/all"))
				.andExpect(status().isOk())
				.andReturn();
		String json = result.getResponse().getContentAsString();
		List<Post> posts = objectMapper.readValue(json, new TypeReference<>(){});

		assertThat(!posts.isEmpty());
		assertThat(posts).hasSize(3);
	}

	@Test
	void shouldGetAllPostsByUserId() throws Exception {
		//this requires a request param in the endpoint url
		MvcResult result = mockMvc.perform(get("/post-api/allByUserId").param("userId", "USER1"))
				.andExpect(status().isOk())
				.andReturn();
		String json = result.getResponse().getContentAsString();
		List<Post> posts = objectMapper.readValue(json, new TypeReference<>(){});

		assertThat(!posts.isEmpty());
		assertThat(posts).hasSize(2);
	}

	@Test
	void shouldGetSinglePost() throws Exception {
		MvcResult result = mockMvc.perform(get("/post-api/post/{id}", 101))
				.andExpect(status().isOk())
				.andReturn();
		String json = result.getResponse().getContentAsString();
		Post post = objectMapper.readValue(json, Post.class);
		Post expectedPost = new Post(101, "USER1", "post1", "post body1", LocalDate.of(2024, 9, 1), false);

		//assertThat(post).isEqualTo(expectedPost);
		assertThat(post).usingRecursiveComparison().ignoringFields("id").isEqualTo(expectedPost);
	}

	@Test
	void shouldThrowExceptionWhenPostIdNotFound() throws Exception {
		mockMvc.perform(get("/post-api/post/{id}", 1010))
				.andExpect(status().isNotFound())
				.andExpect(result -> assertThat(result.getResolvedException()).isInstanceOf(PostNotFoundException.class))
				.andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("Post with id 1010 not found"));
	}

	@Test
	void shouldCreatePostUsingPostEndPoint() throws Exception {
		//String url = BASE_URL + "/object";

		//mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
		Post requestPostObj = new Post("USER1", "New Post1", "post body1", LocalDate.of(2022, 9, 1), false);
		String requestJson=ow.writeValueAsString(requestPostObj);
		System.out.println(requestJson);

		MvcResult result = mockMvc.perform(post("/post-api/createPost")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isCreated())
				.andReturn();

		String json = result.getResponse().getContentAsString();
		Post post = objectMapper.readValue(json, Post.class);
		assertThat(post).usingRecursiveComparison().ignoringFields("id").isEqualTo(requestPostObj);
	}

	@Test
	void shouldUpdatePostUsingPutEndPoint() throws Exception {
		//String url = BASE_URL + "/object";

		//mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
		Post requestPostObj = new Post("USER1", "New Post9999", "post body1", LocalDate.of(2022, 9, 1), false);
		String requestJson=ow.writeValueAsString(requestPostObj);
		System.out.println(requestJson);

		MvcResult result = mockMvc.perform(put("/post-api/updatePost/{id}", 101)
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestJson))
				.andExpect(status().isAccepted())
				.andReturn();

		String json = result.getResponse().getContentAsString();
		Post post = objectMapper.readValue(json, Post.class);
		assertThat(post).usingRecursiveComparison().ignoringFields("id").isEqualTo(requestPostObj);
	}
}
