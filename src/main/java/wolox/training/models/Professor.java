package wolox.training.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(value = "professor")
@Data
@NoArgsConstructor
public class Professor extends User {

    private String subject;
}
