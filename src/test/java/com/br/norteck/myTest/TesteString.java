package com.br.norteck.myTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TesteString {

    @Test
    public void ignorarCasos(){
    String t1 = "testé";
    String t2 = "Teste";
    if(t1.equalsIgnoreCase(t2)){
        System.out.println("Testado com sucesso");
    }
        System.out.println("fora do if");
    }
}
