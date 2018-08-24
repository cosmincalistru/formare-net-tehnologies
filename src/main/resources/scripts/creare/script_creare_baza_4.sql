create table public.users
(
    id bigint not null default nextval('users_seq'::regclass),
    username text collate pg_catalog."default",
    password text collate pg_catalog."default",
    constraint users_pkey primary key (id)
);

create table public.projects
(
    id bigint not null default nextval('projects_seq'::regclass),
    name text collate pg_catalog."default",
    client text collate pg_catalog."default",
    date_start date,
    date_end date,
    constraint projects_pkey primary key (id)
);

create table employees (
    id bigint not null default nextval('employees_seq'::regclass),
	id_user integer,
    first_name text collate pg_catalog."default",
    last_name text collate pg_catalog."default",
    email text collate pg_catalog."default",
    birth_date date,
    id_projects integer,
    constraint employees_pkey primary key (id),
    constraint id_projects foreign key (id_projects)
        references public.projects (id) match simple
        on update no action
        on delete no action,
    constraint id_user foreign key (id_user)
        references public.users (id) match simple
        on update no action
        on delete no action
);

CREATE TABLE public.workers
(
    id bigint NOT NULL DEFAULT nextval('workers_seq'::regclass),
    date_start date,
    date_end date,
    id_employees bigint,
    id_projects bigint,
    CONSTRAINT workers_pkey PRIMARY KEY (id),
    CONSTRAINT id_employees FOREIGN KEY (id_employees)
        REFERENCES public.employees (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT id_projects FOREIGN KEY (id_projects)
        REFERENCES public.projects (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.workers
    OWNER to postgres;

-- Index: fki_id_employees

-- DROP INDEX public.fki_id_employees;

CREATE INDEX fki_id_employees
    ON public.workers USING btree
    (id_employees)
    TABLESPACE pg_default;

-- Index: fki_id_projects

-- DROP INDEX public.fki_id_projects;

CREATE INDEX fki_id_projects
    ON public.workers USING btree
    (id_projects)
    TABLESPACE pg_default;
	
CREATE SEQUENCE public.employees_seq;

ALTER SEQUENCE public.employees_seq
    OWNER TO postgres;

CREATE SEQUENCE public.projects_seq;

ALTER SEQUENCE public.projects_seq
    OWNER TO postgres;

CREATE SEQUENCE public.users_seq;

ALTER SEQUENCE public.users_seq
    OWNER TO postgres;

CREATE SEQUENCE public.workers_seq;

ALTER SEQUENCE public.workers_seq
    OWNER TO postgres;	