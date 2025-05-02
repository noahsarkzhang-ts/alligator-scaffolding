--
-- PostgreSQL database dump
--

-- Dumped from database version 16.2
-- Dumped by pg_dump version 16.2

-- Started on 2025-05-02 19:05:11

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE rpc_scaffolding;
--
-- TOC entry 4810 (class 1262 OID 44578)
-- Name: rpc_scaffolding; Type: DATABASE; Schema: -; Owner: -
--

CREATE DATABASE rpc_scaffolding WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Chinese (Simplified)_China.936';


\connect rpc_scaffolding

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 4 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 4811 (class 0 OID 0)
-- Dependencies: 4
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 44594)
-- Name: t_server_online; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.t_server_online (
    id bigint NOT NULL,
    server_id character varying(32) NOT NULL,
    topic character varying(32) NOT NULL,
    status smallint NOT NULL,
    login_time timestamp without time zone NOT NULL,
    last_ping_time timestamp without time zone NOT NULL,
    type smallint DEFAULT 1 NOT NULL
);


--
-- TOC entry 4812 (class 0 OID 0)
-- Dependencies: 217
-- Name: TABLE t_server_online; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.t_server_online IS '网关在线表';


--
-- TOC entry 4813 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN t_server_online.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_server_online.id IS '主键,使用雪花id';


--
-- TOC entry 4814 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN t_server_online.server_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_server_online.server_id IS '服务id';


--
-- TOC entry 4815 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN t_server_online.topic; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_server_online.topic IS '通信mq topic';


--
-- TOC entry 4816 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN t_server_online.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_server_online.status IS '状态,1：在线,2：离线';


--
-- TOC entry 4817 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN t_server_online.login_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_server_online.login_time IS '登陆时间';


--
-- TOC entry 4818 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN t_server_online.last_ping_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_server_online.last_ping_time IS '上次心跳时间';


--
-- TOC entry 4819 (class 0 OID 0)
-- Dependencies: 217
-- Name: COLUMN t_server_online.type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_server_online.type IS '服务器类型,1:平台网关，2：低端安全帽网关；';


--
-- TOC entry 216 (class 1259 OID 44588)
-- Name: t_subject_login_event; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.t_subject_login_event (
    id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    customer_id bigint NOT NULL,
    subject_type smallint NOT NULL,
    subject_id character varying(32) NOT NULL,
    event_type smallint NOT NULL,
    event_cause smallint NOT NULL,
    event_time timestamp without time zone NOT NULL,
    client_type smallint DEFAULT 0 NOT NULL
);


--
-- TOC entry 4820 (class 0 OID 0)
-- Dependencies: 216
-- Name: TABLE t_subject_login_event; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.t_subject_login_event IS '用户登陆事件表';


--
-- TOC entry 4821 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.id IS '主键,主键';


--
-- TOC entry 4822 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.tenant_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.tenant_id IS '租户id';


--
-- TOC entry 4823 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.customer_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.customer_id IS '客户id';


--
-- TOC entry 4824 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.subject_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.subject_type IS '用户类型,1:设备，2：管理员；3：用户';


--
-- TOC entry 4825 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.subject_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.subject_id IS '用户主体，sn/用户id';


--
-- TOC entry 4826 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.event_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.event_type IS '事件类型,1:上线，2：下线';


--
-- TOC entry 4827 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.event_cause; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.event_cause IS '事件原因,1：正常上线/正常下线,2：断连下线；3：挤占下线';


--
-- TOC entry 4828 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.event_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.event_time IS '事件时间';


--
-- TOC entry 4829 (class 0 OID 0)
-- Dependencies: 216
-- Name: COLUMN t_subject_login_event.client_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_login_event.client_type IS '登陆类型，1:Android,2:IOS,3:Web';


--
-- TOC entry 218 (class 1259 OID 44600)
-- Name: t_subject_online; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.t_subject_online (
    id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    customer_id bigint NOT NULL,
    type smallint NOT NULL,
    subject_id character varying(32) NOT NULL,
    server_id character varying(32) NOT NULL,
    status smallint NOT NULL,
    login_time timestamp without time zone NOT NULL,
    client_type smallint DEFAULT 0 NOT NULL
);


--
-- TOC entry 4830 (class 0 OID 0)
-- Dependencies: 218
-- Name: TABLE t_subject_online; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.t_subject_online IS '用户在线表';


--
-- TOC entry 4831 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.id IS '主键,主键';


--
-- TOC entry 4832 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.tenant_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.tenant_id IS '租户id';


--
-- TOC entry 4833 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.customer_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.customer_id IS '客户id';


--
-- TOC entry 4834 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.type IS '登陆类型,1:设备，2：管理员；3：用户';


--
-- TOC entry 4835 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.subject_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.subject_id IS '登录主体';


--
-- TOC entry 4836 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.server_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.server_id IS '登录所在网关id';


--
-- TOC entry 4837 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.status IS '状态,1：在线,2：离线';


--
-- TOC entry 4838 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.login_time; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.login_time IS '登陆时间';


--
-- TOC entry 4839 (class 0 OID 0)
-- Dependencies: 218
-- Name: COLUMN t_subject_online.client_type; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_subject_online.client_type IS '登陆类型，1:Android,2:IOS,3:Web';


--
-- TOC entry 215 (class 1259 OID 44579)
-- Name: t_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.t_user (
    id bigint NOT NULL,
    tenant_id bigint NOT NULL,
    customer_id bigint NOT NULL,
    dept_id bigint NOT NULL,
    name character varying(64) NOT NULL,
    nickname character varying(64),
    password character varying(128),
    gender smallint NOT NULL,
    avatar character varying(256),
    mobile character varying(16),
    email character varying(64),
    status smallint NOT NULL,
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    operated_by bigint NOT NULL
);


--
-- TOC entry 4840 (class 0 OID 0)
-- Dependencies: 215
-- Name: TABLE t_user; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON TABLE public.t_user IS '用户表';


--
-- TOC entry 4841 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.id IS '主键，使用雪花id';


--
-- TOC entry 4842 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.tenant_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.tenant_id IS '租户id';


--
-- TOC entry 4843 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.customer_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.customer_id IS '客户id';


--
-- TOC entry 4844 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.dept_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.dept_id IS '部门id';


--
-- TOC entry 4845 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.name; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.name IS '名称';


--
-- TOC entry 4846 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.nickname; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.nickname IS '昵称';


--
-- TOC entry 4847 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.password; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.password IS '密码';


--
-- TOC entry 4848 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.gender; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.gender IS '性别';


--
-- TOC entry 4849 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.avatar; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.avatar IS '用户头像';


--
-- TOC entry 4850 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.mobile; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.mobile IS '手机';


--
-- TOC entry 4851 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.email; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.email IS 'email';


--
-- TOC entry 4852 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.status; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.status IS '状态，1：正常 ；2：禁用；';


--
-- TOC entry 4853 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.created_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.created_at IS '创建时间';


--
-- TOC entry 4854 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.updated_at; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.updated_at IS '更新时间';


--
-- TOC entry 4855 (class 0 OID 0)
-- Dependencies: 215
-- Name: COLUMN t_user.operated_by; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.t_user.operated_by IS '操作人';


--
-- TOC entry 4803 (class 0 OID 44594)
-- Dependencies: 217
-- Data for Name: t_server_online; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 4802 (class 0 OID 44588)
-- Dependencies: 216
-- Data for Name: t_subject_login_event; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 4804 (class 0 OID 44600)
-- Dependencies: 218
-- Data for Name: t_subject_online; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 4801 (class 0 OID 44579)
-- Dependencies: 215
-- Data for Name: t_user; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.t_user VALUES (1821383295201558529, 1821382815037636609, 1821383295130255362, 1, 'allen', 'allen', '123456', 1, '', NULL, NULL, 1, '2025-05-02 11:24:01.302365', '2025-05-02 11:24:01.302365', 1);


--
-- TOC entry 4655 (class 2606 OID 44599)
-- Name: t_server_online server_online_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.t_server_online
    ADD CONSTRAINT server_online_pk PRIMARY KEY (id);


--
-- TOC entry 4653 (class 2606 OID 44593)
-- Name: t_subject_login_event subject_login_event_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.t_subject_login_event
    ADD CONSTRAINT subject_login_event_pk PRIMARY KEY (id);


--
-- TOC entry 4657 (class 2606 OID 44605)
-- Name: t_subject_online subject_online_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.t_subject_online
    ADD CONSTRAINT subject_online_pk PRIMARY KEY (id);


--
-- TOC entry 4649 (class 2606 OID 44585)
-- Name: t_user t_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.t_user
    ADD CONSTRAINT t_user_pkey PRIMARY KEY (id);


--
-- TOC entry 4651 (class 2606 OID 44587)
-- Name: t_user t_user_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.t_user
    ADD CONSTRAINT t_user_un UNIQUE (customer_id, name);


-- Completed on 2025-05-02 19:05:12

--
-- PostgreSQL database dump complete
--

