package ma.enset.bankingbackend.dtos;

import lombok.Data;


@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;

}
