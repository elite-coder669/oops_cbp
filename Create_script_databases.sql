-- Table: public.complaint

-- DROP TABLE IF EXISTS public.complaint;

CREATE TABLE IF NOT EXISTS public.complaint
(
    cname character varying(50) COLLATE pg_catalog."default",
    cstatus boolean DEFAULT false,
    department character varying(20) COLLATE pg_catalog."default",
    student_id character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT fk_student FOREIGN KEY (student_id)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.complaint
    OWNER to postgres;

-- Table: public.fee

-- DROP TABLE IF EXISTS public.fee;

CREATE TABLE IF NOT EXISTS public.fee
(
    fee_id integer NOT NULL DEFAULT nextval('fee_fee_id_seq'::regclass),
    stid character varying COLLATE pg_catalog."default" NOT NULL,
    amount_paid numeric(10,2),
    payment_date date,
        CONSTRAINT fk_student FOREIGN KEY (stid)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.fee
    OWNER to postgres;

-- Table: public.room

-- DROP TABLE IF EXISTS public.room;

CREATE TABLE IF NOT EXISTS public.room
(
    rno integer NOT NULL,
    rsize integer,
    vacancies integer,
    CONSTRAINT room_pkey PRIMARY KEY (rno)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.room
    OWNER to postgres;


-- Table: public.student

-- DROP TABLE IF EXISTS public.student;

CREATE TABLE IF NOT EXISTS public.student
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    course character varying COLLATE pg_catalog."default",
    year integer,
    feepending integer,
    password character varying COLLATE pg_catalog."default",
    room integer,
    mobilenumber character(10) COLLATE pg_catalog."default",
    parentmobilenumber character(10) COLLATE pg_catalog."default",
    CONSTRAINT students_pkey PRIMARY KEY (id),
    CONSTRAINT fk_rno FOREIGN KEY (room)
        REFERENCES public.room (rno) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT chk_mobilenumber CHECK (mobilenumber ~ '^\d{10}$'::text),
    CONSTRAINT chk_parentmobilenumber CHECK (parentmobilenumber ~ '^\d{10}$'::text)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.student
    OWNER to postgres;
ALTER TABLE IF EXISTS public.student
    OWNER to postgres;
