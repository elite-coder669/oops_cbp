CREATE TABLE IF NOT EXISTS public.room
(
    rno INTEGER NOT NULL,                 -- Room number (Primary key)
    rsize INTEGER,                         -- Room size (e.g., how many students can fit)
    vacancies INTEGER,                     -- Vacancies available in the room
    CONSTRAINT room_pkey PRIMARY KEY (rno)  -- Primary key constraint
);
CREATE TABLE IF NOT EXISTS public.staff
(
    stid CHARACTER VARYING(4) NOT NULL,        -- Staff ID (Primary key)
    stname CHARACTER VARYING(20),              -- Staff name
    CONSTRAINT staff_pkey PRIMARY KEY (stid)   -- Primary key constraint
);
CREATE TABLE IF NOT EXISTS public.student
(
    id CHARACTER VARYING NOT NULL,             -- Student ID (Primary key)
    name CHARACTER VARYING NOT NULL,           -- Student Name
    course CHARACTER VARYING,                  -- Student's course
    year INTEGER,                              -- Year of study
    feepending INTEGER,                        -- Pending fees
    password CHARACTER VARYING,                -- Student password (could be hashed)
    room INTEGER,                              -- Room number (Foreign key)
    CONSTRAINT students_pkey PRIMARY KEY (id), -- Primary key constraint
    CONSTRAINT fk_rno FOREIGN KEY (room)      -- Foreign key to room table
        REFERENCES public.room (rno)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE TABLE IF NOT EXISTS public.fee
(
    fee_id integer NOT NULL DEFAULT nextval('fee_fee_id_seq'::regclass),
    stid character varying COLLATE pg_catalog."default" NOT NULL,
    amount_paid numeric(10,2),
    payment_date date,
    CONSTRAINT fee_pkey PRIMARY KEY (fee_id),
    CONSTRAINT fk_student FOREIGN KEY (stid)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS public.complaint
(
    cname CHARACTER VARYING(50),         -- Complaint name/description
    cstatus BOOLEAN DEFAULT false,       -- Complaint status (e.g., resolved or pending)
    department CHARACTER VARYING(20),    -- Department handling the complaint
    student_id CHARACTER VARYING(50),    -- Student ID (Foreign key)
    CONSTRAINT fk_student FOREIGN KEY (student_id)   -- Foreign key to student table
        REFERENCES public.student (id)
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

-- For fee_id sequence if required (already defined in your CREATE statement)
CREATE SEQUENCE IF NOT EXISTS fee_fee_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Sequence for auto-generating fee_id (if not already created)
ALTER SEQUENCE fee_fee_id_seq
    OWNED BY public.fee.fee_id;
