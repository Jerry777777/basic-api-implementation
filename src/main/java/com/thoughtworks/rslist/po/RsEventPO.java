package com.thoughtworks.rslist.po;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rs_event")
public class RsEventPO {
    @Id
    @GeneratedValue()
    private Integer id;
    private String eventName;
    private String keyword;
    @ManyToOne(targetEntity = UserPO.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserPO userPO;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "rsEvent")
    private List<VotePO> votes;

    @JsonBackReference
    public UserPO getUserPO() {
        return userPO;
    }

    @JsonBackReference
    public void setUserPO(UserPO userPO) {
        this.userPO = userPO;
    }
}
