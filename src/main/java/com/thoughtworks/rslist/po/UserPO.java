package com.thoughtworks.rslist.po;

import com.thoughtworks.rslist.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class UserPO {
    @Id
    @GeneratedValue()
    private Integer id;
    @Column(name = "name")
    private String userName;
    private Gender gender;
    private Integer age;
    private String email;
    private String phone;
    private Integer voteNum;
}
