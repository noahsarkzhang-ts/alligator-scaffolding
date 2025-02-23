-- public.t_user definition

-- Drop table

-- DROP TABLE public.t_user;

CREATE TABLE public.t_user (
	id int8 NOT NULL, -- 主键，使用雪花id
	tenant_id int8 NOT NULL, -- 租户id
	customer_id int8 NOT NULL, -- 客户id
	dept_id int8 NOT NULL, -- 部门id
	"name" varchar(64) NOT NULL, -- 名称
	nickname varchar(64) NULL, -- 昵称
	"password" varchar(128) NULL, -- 密码
	gender int2 NOT NULL, -- 性别
	avatar varchar(256) NULL, -- 用户头像
	mobile varchar(16) NULL, -- 手机
	email varchar(64) NULL, -- email
	status int2 NOT NULL, -- 状态，1：正常 ；2：禁用；
	created_at timestamp NOT NULL, -- 创建时间
	updated_at timestamp NOT NULL, -- 更新时间
	operated_by int8 NOT NULL, -- 操作人
	CONSTRAINT t_user_pkey PRIMARY KEY (id),
	CONSTRAINT t_user_un UNIQUE (customer_id, name)
);
COMMENT ON TABLE public.t_user IS '用户表';

-- Column comments

COMMENT ON COLUMN public.t_user.id IS '主键，使用雪花id';
COMMENT ON COLUMN public.t_user.tenant_id IS '租户id';
COMMENT ON COLUMN public.t_user.customer_id IS '客户id';
COMMENT ON COLUMN public.t_user.dept_id IS '部门id';
COMMENT ON COLUMN public.t_user."name" IS '名称';
COMMENT ON COLUMN public.t_user.nickname IS '昵称';
COMMENT ON COLUMN public.t_user."password" IS '密码';
COMMENT ON COLUMN public.t_user.gender IS '性别';
COMMENT ON COLUMN public.t_user.avatar IS '用户头像';
COMMENT ON COLUMN public.t_user.mobile IS '手机';
COMMENT ON COLUMN public.t_user.email IS 'email';
COMMENT ON COLUMN public.t_user.status IS '状态，1：正常 ；2：禁用；';
COMMENT ON COLUMN public.t_user.created_at IS '创建时间';
COMMENT ON COLUMN public.t_user.updated_at IS '更新时间';
COMMENT ON COLUMN public.t_user.operated_by IS '操作人';