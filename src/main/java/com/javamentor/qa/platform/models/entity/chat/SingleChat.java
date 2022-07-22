package com.javamentor.qa.platform.models.entity.chat;


import com.javamentor.qa.platform.exception.ApiRequestException;
import com.javamentor.qa.platform.models.entity.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "single_chat")
public class SingleChat{

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @MapsId
    private Chat chat = new Chat(ChatType.SINGLE);

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User userOne;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User useTwo;

    @PrePersist
    private void prePersistFunction() {
        checkConstraints();
    }

    @PreUpdate
    private void preUpdateFunction() {
        checkConstraints();
    }

    private void checkConstraints() {
        if (this.chat.getChatType() != ChatType.SINGLE) {
            throw new ApiRequestException("У экземпляра Chat, связанного с SingleChat, " +
                    "поле chatType должно принимать значение ChatType.SINGLE");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SingleChat that = (SingleChat) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(chat, that.chat) &&
                Objects.equals(userOne, that.userOne) &&
                Objects.equals(useTwo, that.useTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chat, userOne, useTwo);
    }
}
