package org.aom.post_service.post.dto;


import java.time.LocalDate;

public class CreateUpdatePostRequestDto {
    private String userId;
    private String title;
    private String body;
    private LocalDate dateAdded;
    private Boolean isBlocked;

    public CreateUpdatePostRequestDto() {
    }

    public CreateUpdatePostRequestDto(String userId, String title, String body, LocalDate dateAdded, Boolean isBlocked) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.dateAdded = dateAdded;
        this.isBlocked = isBlocked;
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

    public void setBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", dateAdded=" + dateAdded +
                ", isBlocked=" + isBlocked +
                '}';
    }
}
