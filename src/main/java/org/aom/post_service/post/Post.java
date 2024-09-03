package org.aom.post_service.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
@Entity
public class Post {
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq_gen")
        @SequenceGenerator(name = "post_seq_gen", sequenceName = "post_seq", allocationSize = 1)
        private Integer id;
        private String userId;
        @NotEmpty
        private String title;
        @NotEmpty
        private String body;
        @Column(name = "date_added")
        private LocalDate dateAdded;
        @Column(name = "is_blocked")
        private Boolean isBlocked;

        public Post() {
        }
        public Post(String userId, String title, String body, LocalDate dateAdded, Boolean isBlocked) {
                this.userId = userId;
                this.title = title;
                this.body = body;
                this.dateAdded = dateAdded;
                this.isBlocked = isBlocked;
        }

        public Post(Integer id, String userId, String title, String body, LocalDate dateAdded, Boolean isBlocked) {
                this.id = id;
                this.userId = userId;
                this.title = title;
                this.body = body;
                this.dateAdded = dateAdded;
                this.isBlocked = isBlocked;
        }

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getUserId() {
                return userId;
        }

        public void setUserId(String userId) {
                this.userId = userId;
        }

        public String getTitle() {
                return title;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getBody() {
                return body;
        }

        public void setBody(String body) {
                this.body = body;
        }

        public LocalDate getDateAdded() {
                return dateAdded;
        }

        public void setDateAdded(LocalDate dateAdded) {
                this.dateAdded = dateAdded;
        }

        public Boolean getBlocked() {
                return isBlocked;
        }
        @JsonProperty("isBlocked")
        public void setBlocked(Boolean blocked) {
                isBlocked = blocked;
        }

        @Override
        public String toString() {
                return "Post{" +
                        "id=" + id +
                        ", userId=" + userId +
                        ", title='" + title + '\'' +
                        ", body='" + body + '\'' +
                        ", dateAdded=" + dateAdded +
                        ", isBlocked=" + isBlocked +
                        '}';
        }
}