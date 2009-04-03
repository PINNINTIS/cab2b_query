drop table COMMONS_GRAPH cascade constraints;
drop table COMMONS_GRAPH_EDGE cascade constraints;
drop table COMMONS_GRAPH_TO_EDGES cascade constraints;
drop table COMMONS_GRAPH_TO_VERTICES cascade constraints;
drop table QUERY cascade constraints;
drop table QUERY_ARITHMETIC_OPERAND cascade constraints;
drop table QUERY_BASEEXPR_TO_CONNECTORS cascade constraints;
drop table QUERY_BASE_EXPRESSION cascade constraints;
drop table QUERY_BASE_EXPR_OPND cascade constraints;
drop table QUERY_CONDITION cascade constraints;
drop table QUERY_CONDITION_VALUES cascade constraints;
drop table QUERY_CONNECTOR cascade constraints;
drop table QUERY_CONSTRAINTS cascade constraints;
drop table QUERY_CONSTRAINT_TO_EXPR cascade constraints;
drop table QUERY_CUSTOM_FORMULA cascade constraints;
drop table QUERY_EXPRESSION cascade constraints;
drop table QUERY_FORMULA_RHS cascade constraints;
drop table QUERY_INTER_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_INTRA_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_JOIN_GRAPH cascade constraints;
drop table QUERY_MODEL_ASSOCIATION cascade constraints;
drop table QUERY_OPERAND cascade constraints;
drop table QUERY_OUTPUT_ATTRIBUTE cascade constraints;
drop table QUERY_OUTPUT_TERM cascade constraints;
drop table QUERY_PARAMETER cascade constraints;
drop table QUERY_PARAMETERIZED_QUERY cascade constraints;
drop table QUERY_QUERY_ENTITY cascade constraints;
drop table QUERY_RULE_COND cascade constraints;
drop table QUERY_SUBEXPR_OPERAND cascade constraints;
drop table QUERY_TO_OUTPUT_TERMS cascade constraints;
drop table QUERY_TO_PARAMETERS cascade constraints;
drop sequence COMMONS_GRAPH_EDGE_SEQ;
drop sequence COMMONS_GRAPH_SEQ;
drop sequence QUERY_BASE_EXPRESSION_SEQ;
drop sequence QUERY_CONDITION_SEQ;
drop sequence QUERY_CONNECTOR_SEQ;
drop sequence QUERY_CONSTRAINT_SEQ;
drop sequence QUERY_ENTITY_SEQ;
drop sequence QUERY_JOIN_GRAPH_SEQ;
drop sequence QUERY_MODEL_ASSOCIATION_SEQ;
drop sequence QUERY_OPERAND_SEQ;
drop sequence QUERY_OUTPUT_ATTRIBUTE_SEQ;
drop sequence QUERY_OUTPUT_TERM_SEQ;
drop sequence QUERY_PARAMETER_SEQ;
drop sequence QUERY_SEQ;
create table COMMONS_GRAPH (IDENTIFIER number(19,0) not null, primary key (IDENTIFIER));
create table COMMONS_GRAPH_EDGE (IDENTIFIER number(19,0) not null, SOURCE_VERTEX_CLASS varchar2(255 char), SOURCE_VERTEX_ID number(19,0), TARGET_VERTEX_CLASS varchar2(255 char), TARGET_VERTEX_ID number(19,0), EDGE_CLASS varchar2(255 char), EDGE_ID number(19,0), primary key (IDENTIFIER));
create table COMMONS_GRAPH_TO_EDGES (GRAPH_ID number(19,0) not null, EDGE_ID number(19,0) not null unique, primary key (GRAPH_ID, EDGE_ID));
create table COMMONS_GRAPH_TO_VERTICES (GRAPH_ID number(19,0) not null, VERTEX_CLASS varchar2(255 char), VERTEX_ID number(19,0));
create table QUERY (IDENTIFIER number(19,0) not null, CONSTRAINTS_ID number(19,0) unique, primary key (IDENTIFIER));
create table QUERY_ARITHMETIC_OPERAND (IDENTIFIER number(19,0) not null, LITERAL varchar2(255 char), TERM_TYPE varchar2(255 char), DATE_LITERAL date, TIME_INTERVAL varchar2(255 char), DE_ATTRIBUTE_ID number(19,0), EXPRESSION_ID number(19,0), primary key (IDENTIFIER));
create table QUERY_BASEEXPR_TO_CONNECTORS (BASE_EXPRESSION_ID number(19,0) not null, CONNECTOR_ID number(19,0) not null, POSITION number(10,0) not null, primary key (BASE_EXPRESSION_ID, POSITION));
create table QUERY_BASE_EXPRESSION (IDENTIFIER number(19,0) not null, EXPR_TYPE varchar2(255 char) not null, primary key (IDENTIFIER));
create table QUERY_BASE_EXPR_OPND (BASE_EXPRESSION_ID number(19,0) not null, OPERAND_ID number(19,0) not null, POSITION number(10,0) not null, primary key (BASE_EXPRESSION_ID, POSITION));
create table QUERY_CONDITION (IDENTIFIER number(19,0) not null, ATTRIBUTE_ID number(19,0) not null, RELATIONAL_OPERATOR varchar2(255 char), primary key (IDENTIFIER));
create table QUERY_CONDITION_VALUES (CONDITION_ID number(19,0) not null, VALUE varchar2(255 char), POSITION number(10,0) not null, primary key (CONDITION_ID, POSITION));
create table QUERY_CONNECTOR (IDENTIFIER number(19,0) not null, OPERATOR varchar2(255 char), NESTING_NUMBER number(10,0), primary key (IDENTIFIER));
create table QUERY_CONSTRAINTS (IDENTIFIER number(19,0) not null, QUERY_JOIN_GRAPH_ID number(19,0) unique, primary key (IDENTIFIER));
create table QUERY_CONSTRAINT_TO_EXPR (CONSTRAINT_ID number(19,0) not null, EXPRESSION_ID number(19,0) not null unique, primary key (CONSTRAINT_ID, EXPRESSION_ID));
create table QUERY_CUSTOM_FORMULA (IDENTIFIER number(19,0) not null, OPERATOR varchar2(255 char), LHS_TERM_ID number(19,0), primary key (IDENTIFIER));
create table QUERY_EXPRESSION (IDENTIFIER number(19,0) not null, IS_IN_VIEW number(1,0), IS_VISIBLE number(1,0), UI_EXPR_ID number(10,0), QUERY_ENTITY_ID number(19,0), primary key (IDENTIFIER));
create table QUERY_FORMULA_RHS (CUSTOM_FORMULA_ID number(19,0) not null, RHS_TERM_ID number(19,0) not null, POSITION number(10,0) not null, primary key (CUSTOM_FORMULA_ID, POSITION));
create table QUERY_INTER_MODEL_ASSOCIATION (IDENTIFIER number(19,0) not null, SOURCE_SERVICE_URL varchar2(1000 char) not null, TARGET_SERVICE_URL varchar2(1000 char) not null, SOURCE_ATTRIBUTE_ID number(19,0) not null, TARGET_ATTRIBUTE_ID number(19,0) not null, primary key (IDENTIFIER));
create table QUERY_INTRA_MODEL_ASSOCIATION (IDENTIFIER number(19,0) not null, DE_ASSOCIATION_ID number(19,0) not null, primary key (IDENTIFIER));
create table QUERY_JOIN_GRAPH (IDENTIFIER number(19,0) not null, COMMONS_GRAPH_ID number(19,0), primary key (IDENTIFIER));
create table QUERY_MODEL_ASSOCIATION (IDENTIFIER number(19,0) not null, primary key (IDENTIFIER));
create table QUERY_OPERAND (IDENTIFIER number(19,0) not null, OPND_TYPE varchar2(255 char) not null, primary key (IDENTIFIER));
create table QUERY_OUTPUT_ATTRIBUTE (IDENTIFIER number(19,0) not null, EXPRESSION_ID number(19,0), ATTRIBUTE_ID number(19,0) not null, PARAMETERIZED_QUERY_ID number(19,0), POSITION number(10,0), primary key (IDENTIFIER));
create table QUERY_OUTPUT_TERM (IDENTIFIER number(19,0) not null, NAME varchar2(255 char), TIME_INTERVAL varchar2(255 char), TERM_ID number(19,0), primary key (IDENTIFIER));
create table QUERY_PARAMETER (IDENTIFIER number(19,0) not null, NAME varchar2(255 char), OBJECT_CLASS varchar2(255 char), OBJECT_ID number(19,0), primary key (IDENTIFIER));
create table QUERY_PARAMETERIZED_QUERY (IDENTIFIER number(19,0) not null, QUERY_NAME varchar2(255 char) unique, DESCRIPTION varchar2(1024 char), primary key (IDENTIFIER));
create table QUERY_QUERY_ENTITY (IDENTIFIER number(19,0) not null, ENTITY_ID number(19,0) not null, primary key (IDENTIFIER));
create table QUERY_RULE_COND (RULE_ID number(19,0) not null, CONDITION_ID number(19,0) not null, POSITION number(10,0) not null, primary key (RULE_ID, POSITION));
create table QUERY_SUBEXPR_OPERAND (IDENTIFIER number(19,0) not null, EXPRESSION_ID number(19,0), primary key (IDENTIFIER));
create table QUERY_TO_OUTPUT_TERMS (QUERY_ID number(19,0) not null, OUTPUT_TERM_ID number(19,0) not null unique, POSITION number(10,0) not null, primary key (QUERY_ID, POSITION));
create table QUERY_TO_PARAMETERS (QUERY_ID number(19,0) not null, PARAMETER_ID number(19,0) not null unique, POSITION number(10,0) not null, primary key (QUERY_ID, POSITION));
alter table COMMONS_GRAPH_TO_EDGES add constraint FKA6B0D8BAA0494B1D foreign key (GRAPH_ID) references COMMONS_GRAPH;
alter table COMMONS_GRAPH_TO_EDGES add constraint FKA6B0D8BAFAEF80D foreign key (EDGE_ID) references COMMONS_GRAPH_EDGE;
alter table COMMONS_GRAPH_TO_VERTICES add constraint FK2C4412F5A0494B1D foreign key (GRAPH_ID) references COMMONS_GRAPH;
alter table QUERY add constraint FK49D20A89E2FD9C7 foreign key (CONSTRAINTS_ID) references QUERY_CONSTRAINTS;
alter table QUERY_ARITHMETIC_OPERAND add constraint FK262AEB0BD635BD31 foreign key (IDENTIFIER) references QUERY_OPERAND;
alter table QUERY_ARITHMETIC_OPERAND add constraint FK262AEB0BE92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_BASEEXPR_TO_CONNECTORS add constraint FK3F0043482FCE1DA7 foreign key (CONNECTOR_ID) references QUERY_CONNECTOR;
alter table QUERY_BASEEXPR_TO_CONNECTORS add constraint FK3F00434848BA6890 foreign key (BASE_EXPRESSION_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_BASE_EXPR_OPND add constraint FKAE67EAF0712A4C foreign key (OPERAND_ID) references QUERY_OPERAND;
alter table QUERY_BASE_EXPR_OPND add constraint FKAE67EA48BA6890 foreign key (BASE_EXPRESSION_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_CONDITION_VALUES add constraint FK9997379D6458C2E7 foreign key (CONDITION_ID) references QUERY_CONDITION;
alter table QUERY_CONSTRAINTS add constraint FKE364FCFF1C7EBF3B foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH;
alter table QUERY_CONSTRAINT_TO_EXPR add constraint FK2BD705CEA0A5F4C0 foreign key (CONSTRAINT_ID) references QUERY_CONSTRAINTS;
alter table QUERY_CONSTRAINT_TO_EXPR add constraint FK2BD705CEE92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_CUSTOM_FORMULA add constraint FK5C0EEAEFBE674D45 foreign key (LHS_TERM_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_CUSTOM_FORMULA add constraint FK5C0EEAEF12D455EB foreign key (IDENTIFIER) references QUERY_OPERAND;
alter table QUERY_EXPRESSION add constraint FK1B473A8F40EB75D4 foreign key (IDENTIFIER) references QUERY_BASE_EXPRESSION;
alter table QUERY_EXPRESSION add constraint FK1B473A8F635766D8 foreign key (QUERY_ENTITY_ID) references QUERY_QUERY_ENTITY;
alter table QUERY_FORMULA_RHS add constraint FKAE90F94D3BC37DCB foreign key (RHS_TERM_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_FORMULA_RHS add constraint FKAE90F94D9A0B7164 foreign key (CUSTOM_FORMULA_ID) references QUERY_OPERAND;
alter table QUERY_INTER_MODEL_ASSOCIATION add constraint FKD70658D15F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION;
alter table QUERY_INTRA_MODEL_ASSOCIATION add constraint FKF1EDBDD35F5AB67E foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION;
alter table QUERY_JOIN_GRAPH add constraint FK2B41B5D09DBC4D94 foreign key (COMMONS_GRAPH_ID) references COMMONS_GRAPH;
alter table QUERY_OUTPUT_ATTRIBUTE add constraint FK22C9DB75604D4BDA foreign key (PARAMETERIZED_QUERY_ID) references QUERY_PARAMETERIZED_QUERY;
alter table QUERY_OUTPUT_ATTRIBUTE add constraint FK22C9DB75E92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_OUTPUT_TERM add constraint FK13C8A3D388C86B0D foreign key (TERM_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_PARAMETERIZED_QUERY add constraint FKA272176B76177EFE foreign key (IDENTIFIER) references QUERY;
alter table QUERY_RULE_COND add constraint FKC32D37AE6458C2E7 foreign key (CONDITION_ID) references QUERY_CONDITION;
alter table QUERY_RULE_COND add constraint FKC32D37AE39F0A10D foreign key (RULE_ID) references QUERY_OPERAND;
alter table QUERY_SUBEXPR_OPERAND add constraint FK2BF760E832E875C8 foreign key (IDENTIFIER) references QUERY_OPERAND;
alter table QUERY_SUBEXPR_OPERAND add constraint FK2BF760E8E92C814D foreign key (EXPRESSION_ID) references QUERY_BASE_EXPRESSION;
alter table QUERY_TO_OUTPUT_TERMS add constraint FK8A70E2565E5B9430 foreign key (OUTPUT_TERM_ID) references QUERY_OUTPUT_TERM;
alter table QUERY_TO_OUTPUT_TERMS add constraint FK8A70E25691051647 foreign key (QUERY_ID) references QUERY;
alter table QUERY_TO_PARAMETERS add constraint FK8060DAD7F84B9027 foreign key (PARAMETER_ID) references QUERY_PARAMETER;
alter table QUERY_TO_PARAMETERS add constraint FK8060DAD739F0A314 foreign key (QUERY_ID) references QUERY_PARAMETERIZED_QUERY;
create sequence COMMONS_GRAPH_EDGE_SEQ;
create sequence COMMONS_GRAPH_SEQ;
create sequence QUERY_BASE_EXPRESSION_SEQ;
create sequence QUERY_CONDITION_SEQ;
create sequence QUERY_CONNECTOR_SEQ;
create sequence QUERY_CONSTRAINT_SEQ;
create sequence QUERY_ENTITY_SEQ;
create sequence QUERY_JOIN_GRAPH_SEQ;
create sequence QUERY_MODEL_ASSOCIATION_SEQ;
create sequence QUERY_OPERAND_SEQ;
create sequence QUERY_OUTPUT_ATTRIBUTE_SEQ;
create sequence QUERY_OUTPUT_TERM_SEQ;
create sequence QUERY_PARAMETER_SEQ;
create sequence QUERY_SEQ;
