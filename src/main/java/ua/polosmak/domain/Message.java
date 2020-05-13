package ua.polosmak.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String text;
	private String tag;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User author;

	public Message() {
	}

	public Message(String text, String tag, User user) {
		this.author = user;
		this.text = text;
		this.tag = tag;
	}

	public String getAuthorName() {
		return author != null ? author.getUsername() : "<none>";
	}

	public Integer getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public String getTag() {
		return tag;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}
}
