-- =====================================================
-- NOVABANK - ESTRUCTURA COMPLETA BASE DE DATOS
-- =====================================================

CREATE DATABASE novabank;

-- =========================
-- ELIMINAR SI EXISTE (ORDEN CORRECTO)
-- =========================

DROP TABLE IF EXISTS public.movements CASCADE;
DROP TABLE IF EXISTS public.accounts CASCADE;
DROP TABLE IF EXISTS public.clients CASCADE;
DROP TABLE IF EXISTS public.users CASCADE;

DROP SEQUENCE IF EXISTS public.clients_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.accounts_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.movements_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.users_id_seq CASCADE;
DROP SEQUENCE IF EXISTS public.iban_seq CASCADE;

-- =========================
-- SECUENCIAS
-- =========================

CREATE SEQUENCE public.clients_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE public.accounts_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE public.movements_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE public.users_id_seq START 1 INCREMENT 1;
CREATE SEQUENCE public.iban_seq START 1 INCREMENT 1;

-- =========================
-- TABLA CLIENTS
-- =========================

CREATE TABLE public.clients (
                                id bigint NOT NULL DEFAULT nextval('public.clients_id_seq'),
                                first_name varchar(255) NOT NULL,
                                last_name varchar(255) NOT NULL,
                                dni varchar(255) NOT NULL,
                                email varchar(255) NOT NULL,
                                phone varchar(255) NOT NULL,
                                CONSTRAINT clients_pkey PRIMARY KEY (id),
                                CONSTRAINT clients_dni_key UNIQUE (dni),
                                CONSTRAINT clients_email_key UNIQUE (email)
);

ALTER SEQUENCE public.clients_id_seq OWNED BY public.clients.id;

-- =========================
-- TABLA ACCOUNTS
-- =========================

CREATE TABLE public.accounts (
                                 id bigint NOT NULL DEFAULT nextval('public.accounts_id_seq'),
                                 iban varchar(255) NOT NULL,
                                 client_id bigint NOT NULL,
                                 balance numeric(38,2) NOT NULL DEFAULT 0,
                                 created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                 CONSTRAINT accounts_pkey PRIMARY KEY (id),
                                 CONSTRAINT accounts_iban_key UNIQUE (iban),
                                 CONSTRAINT chk_balance_non_negative CHECK (balance >= 0),
                                 CONSTRAINT fk_account_client
                                     FOREIGN KEY (client_id)
                                         REFERENCES public.clients(id)
                                         ON DELETE CASCADE
);

ALTER SEQUENCE public.accounts_id_seq OWNED BY public.accounts.id;

-- =========================
-- TABLA MOVEMENTS
-- =========================

CREATE TABLE public.movements (
                                  id bigint NOT NULL DEFAULT nextval('public.movements_id_seq'),
                                  account_id bigint NOT NULL,
                                  type varchar(255) NOT NULL,
                                  amount numeric(38,2) NOT NULL,
                                  created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT movements_pkey PRIMARY KEY (id),
                                  CONSTRAINT chk_amount_positive CHECK (amount > 0),
                                  CONSTRAINT chk_type_valid CHECK (
                                      type IN (
                                               'DEPOSIT',
                                               'WITHDRAW',
                                               'OUTGOING_TRANSFER',
                                               'INCOMING_TRANSFER'
                                          )
                                      ),
                                  CONSTRAINT fk_movement_account
                                      FOREIGN KEY (account_id)
                                          REFERENCES public.accounts(id)
                                          ON DELETE CASCADE
);

ALTER SEQUENCE public.movements_id_seq OWNED BY public.movements.id;

-- =========================
-- TABLA USERS
-- =========================

CREATE TABLE public.users (
                              id bigint NOT NULL DEFAULT nextval('public.users_id_seq'),
                              username varchar(255) NOT NULL,
                              password varchar(255) NOT NULL,
                              role varchar(255) NOT NULL,
                              CONSTRAINT users_pkey PRIMARY KEY (id),
                              CONSTRAINT users_username_key UNIQUE (username)
);

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


