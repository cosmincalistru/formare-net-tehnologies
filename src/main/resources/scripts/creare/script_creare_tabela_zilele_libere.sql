CREATE SEQUENCE public.holidays_seq;

ALTER SEQUENCE public.holidays_seq
    OWNER TO postgres;
	
create table public.holidays
(
    id bigint not null default nextval('holidays_seq'::regclass),
    date_start date,
    id_employees bigint,
	duration decimal,
	free_type text collate pg_catalog."default",
	CONSTRAINT id_employees FOREIGN KEY (id_employees)
        REFERENCES public.employees (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);