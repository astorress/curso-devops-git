package com.paymentchain.billing;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;





@ExtendWith(MockitoExtension.class)
public class BasicApplicationTests {
 
 @Test
 public void contextLoads() {
 	    String message= "default message cambio test devops";
 	   assertNotNull(message);
 }
 
}
