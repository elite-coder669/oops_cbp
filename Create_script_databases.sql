-- Table: public.complaint

DROP TABLE IF EXISTS public.complaint;

CREATE TABLE public.complaint
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

ALTER TABLE public.complaint
    OWNER TO postgres;

-- Table: public.fee

DROP TABLE IF EXISTS public.fee;

CREATE TABLE public.fee
(
    fee_id integer NOT NULL DEFAULT nextval('fee_fee_id_seq'::regclass),
    stid character varying COLLATE pg_catalog."default" NOT NULL,
    amount_paid numeric(10,2),
    payment_date text COLLATE pg_catalog."default",
    CONSTRAINT fee_pkey PRIMARY KEY (fee_id),
    CONSTRAINT fk_student FOREIGN KEY (stid)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)

TABLESPACE pg_default;

ALTER TABLE public.fee
    OWNER TO postgres;

-- Table: public.room

DROP TABLE IF EXISTS public.room;

CREATE TABLE public.room
(
    rno integer NOT NULL,
    rsize integer,
    vacancies integer,
    CONSTRAINT room_pkey PRIMARY KEY (rno)
)

TABLESPACE pg_default;

ALTER TABLE public.room
    OWNER TO postgres;

-- Table: public.staff

DROP TABLE IF EXISTS public.staff;

CREATE TABLE public.staff
(
    stid character varying(4) COLLATE pg_catalog."default" NOT NULL,
    stname character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT staff_pkey PRIMARY KEY (stid)
)

TABLESPACE pg_default;

ALTER TABLE public.staff
    OWNER TO postgres;

-- Table: public.student

DROP TABLE IF EXISTS public.student;

CREATE TABLE public.student
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    course character varying COLLATE pg_catalog."default",
    year integer,
    feepending integer,
    password character varying COLLATE pg_catalog."default",
    room integer,
    CONSTRAINT students_pkey PRIMARY KEY (id),
    CONSTRAINT fk_rno FOREIGN KEY (room)
        REFERENCES public.room (rno) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE SET NULL
)

TABLESPACE pg_default;

ALTER TABLE public.student
    OWNER TO postgres;
