package gr.personal.story.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Nick Kanakis on 4/5/2017.
 */

public class CommentRequest {

    private String header;
    private String userId;
    private String description;
    private Date postDate;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }


    @Override
    public String toString() {
        return "Comment{" +
                " header='" + header + '\'' +
                ", userId='" + userId + '\'' +
                ", description='" + description + '\'' +
                ", postDate=" + postDate +
                '}';
    }
}