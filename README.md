# NorteckFoods
# 🍔 NorteckFoods - Sistema de Gestão para Lanchonetes

[![Java](https://img.shields.io/badge/Java-21-red.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)

## 📌 Visão Geral
Sistema completo para gestão de pedidos, estoque e finanças de 
estabelecimentos alimentícios, desenvolvido com stack moderna Java/Spring.

## 🛠 Stack Tecnológica
| Camada          | Tecnologias                                                                 |
|-----------------|-----------------------------------------------------------------------------|
| **Backend**     | Java 17, Spring Boot 3.1                                                   |
| **Segurança**   | Spring Security, Autenticação Baseada em Roles                             |
| **Persistência**| Spring Data JPA, PostgreSQL 15, Hibernate                                  |
| **API**         | RESTful, JSON, Tratamento avançado de exceções                             |
| **Ferramentas** | Maven, Postman, pgAdmin     

## 🌟 Features Implementadas
### 🛒 Gestão de Pedidos
- Fluxo completo de pedidos (aberto → em preparo → finalizado)
- Pagamentos múltiplos (dinheiro, cartão, pix)
- Cálculo automático de troco

### 📦 Controle de Estoque
- Baixa automática de ingredientes
- Validação de estoque disponível
- Rastreamento de consumo

### 🔒 Sistema Seguro
- Controle de acesso por perfis:
    - `OPERADOR_CAIXA`: Criar pedidos
    - `GERENTE`: Alterar pedidos + relatórios
    - `ADMIN`: Acesso total
    - 
- ## 🗄 Modelo de Dados (Principais Entidades)
