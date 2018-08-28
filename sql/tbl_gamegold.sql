-- Table: "TBL_GAMEGOLD_DISCOUNT"

-- DROP TABLE "TBL_GAMEGOLD_DISCOUNT";

CREATE TABLE "TBL_GAMEGOLD_DISCOUNT"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_discount'::regclass),
  "GOLD_COUNT" integer,
  "DISCOUNT" numeric(10,4),
  "IS_DELETED" boolean,
  "GOODS_ID" bigint,
  CONSTRAINT "TBL_GAMEGOLD_discount_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_DISCOUNT"
  OWNER TO postgres;

-- Table: "TBL_GAMEGOLD_GOODS"

-- DROP TABLE "TBL_GAMEGOLD_GOODS";

CREATE TABLE "TBL_GAMEGOLD_GOODS"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_goods'::regclass),
  "GAME_NAME" character varying,
  "REGION" character varying,
  "SERVER" character varying,
  "TITLE" character varying,
  "UNIT_PRICE" numeric(10,4),
  "LAST_UPDATE_TIME" timestamp without time zone,
  "CREATE_TIME" timestamp without time zone,
  "IS_DELETED" boolean,
  "DELIVERY_TIME" numeric,
  "GOODS_CAT" numeric,
  "SALES" numeric,
  "GAME_RACE" character varying,
  "MONEY_NAME" character varying,
  CONSTRAINT "TBL_GAMEGOLD_GOODS_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_GOODS" 
  OWNER TO postgres;
  
-- Table: "TBL_GAMEGOLD_ORDERS"

-- DROP TABLE "TBL_GAMEGOLD_ORDERS";

CREATE TABLE "TBL_GAMEGOLD_ORDERS"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_orders'::regclass),
  "ORDER_ID" character varying,
  "UID" character varying,
  "SERVICER_ID" bigint,
  "MOBILE_NUMBER" character varying,
  "QQ" character varying,
  "RECEIVER" character varying,
  "GOODS_ID" bigint,
  "GAME_NAME" character varying,
  "MONEY_NAME" character varying,
  "REGION" character varying,
  "SERVER" character varying,
  "TITLE" character varying,
  "GOLD_COUNT" numeric,
  "UNIT_PRICE" numeric(10,4),
  "NOTES" character varying,
  "TOTAL_PRICE" numeric(10,4),
  "ORDER_STATE" numeric,
  "CREATE_TIME" timestamp without time zone,
  "PAY_TIME" timestamp without time zone,
  "SEND_TIME" timestamp without time zone,
  "END_TIME" timestamp without time zone,
  "TRADE_TYPE" integer,
  "DELIVERY_TIME" integer,
  "IS_DELAY" boolean,
  "GAME_RACE" character varying,
  "IS_HAVE_STORE" boolean,
  "USER_ACCOUNT" character varying,
  "GAME_LEVEL" integer,
  CONSTRAINT "TBL_GAMEGOLD_ORDERS_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_ORDERS" 
  OWNER TO postgres;
  
-- Table: "TBL_GAMEGOLD_CONFIGRESULT"
  
-- DROP TABLE "TBL_GAMEGOLD_CONFIGRESULT";

CREATE TABLE "TBL_GAMEGOLD_CONFIGRESULT"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_configresults'::regclass),
  "ORDER_ID" character varying,
  "REPOSITORY_ID" bigint,
  "TRADE_TYPE" integer,
  "SERVICER_ID" bigint,
  "OPTION_ID" bigint,
  "LOGIN_ACCOUNT" character varying,
  "ACCOUNT_UID" character varying,
  "CONFIG_GOLD_COUNT" numeric,
  "ORDER_UNIT_PRICE" numeric(10,4),
  "REPOSITORY_UNIT_PRICE" numeric(10,4),
  "TOTAL_PRICE" numeric(10,4),
  "STATE" numeric,
  "LAST_UPDATE_TIME" timestamp without time zone,
  "CREATE_TIME" timestamp without time zone,
  "IS_DELETED" boolean,
  CONSTRAINT "TBL_GAMEGOLD_CONFIGRESULT_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_CONFIGRESULT"
  OWNER TO postgres;
  
-- Table: "TBL_GAMEGOLD_REPOSITORY"

-- DROP TABLE "TBL_GAMEGOLD_REPOSITORY";

CREATE TABLE "TBL_GAMEGOLD_REPOSITORY"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_repository'::regclass),
  "GAME_NAME" character varying,
  "REGION" character varying,
  "SERVER" character varying,
  "GOLD_COUNT" numeric(10,2),
  "LAST_UPDATE_TIME" timestamp without time zone,
  "CREATE_TIME" timestamp without time zone,
  "IS_DELETED" boolean,
  "LOGIN_ACCOUNT" character varying,
  "SERVICER_ID" bigint,
  "UNIT_PRICE" numeric(10,4),
  "ACCOUNT_UID" character varying,
  "GAME_RACE" character varying,
  "MONEY_NAME" character varying,
  "SELLER_GAME_ROLE" character varying,
  "SON_ACCOUNT" character varying,
  "GAME_ACCOUNT" character varying,
  "GAME_PASSWORD" character varying,
  "SON_GAME_PASSWORD" character varying,
  "PASSPOD_URL" character varying,
  "ROLE_PASSWORD" character varying,
  "FUNDS_PASSWORD" character varying,
  "HOUSE_PASSWORD" character varying,
  "SELLABLE_COUNT" numeric(10,2),
  CONSTRAINT "TBL_GAMEGOLD_REPOSITORY_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_REPOSITORY" 
  OWNER TO postgres;
  
-- Table: "TBL_GAMEGOLD_SELLER"

-- DROP TABLE "TBL_GAMEGOLD_SELLER";

CREATE TABLE "TBL_GAMEGOLD_SELLER"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_seller'::regclass),
  "NAME" character varying,
  "UID" character varying,
  "LOGIN_ACCOUNT" character varying,
  "LAST_UPDATE_TIME" timestamp without time zone,
  "CREATE_TIME" timestamp without time zone,
  "IS_DELETED" boolean,
  "CHECK_STATE" integer,
  "PHONE_NUMBER" character varying,
  "SERVICER_ID" numeric,
  "QQ" character varying,
  "NOTES" character varying,
  CONSTRAINT "TBL_GAMEGOLD_seller_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_SELLER"
  OWNER TO gamegold;
 
-- Table: "TBL_GAMEGOLD_TRADE_PLACE"

-- Table: "TBL_GAMEGOLD_TRADE_PLACE"

-- DROP TABLE "TBL_GAMEGOLD_TRADE_PLACE";

CREATE TABLE "TBL_GAMEGOLD_TRADE_PLACE"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_tradeplace'::regclass),
  "GAME_NAME" character varying,
  "PLACE_NAME" character varying,
  "PLACE_IMAGE" character varying,
  "GAME_IMAGE" character varying,
  "LAST_UPDATE_TIME" timestamp without time zone,
  "CREATE_TIME" timestamp without time zone,
  "IS_DELETED" boolean,
  "MAIL_TIME" integer,
  "AUTO_PLAY_TIME" integer,
  CONSTRAINT "TBL_GAMEGOLD_TRADE_PLACE_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_TRADE_PLACE"
  OWNER TO postgres;

-- Table: "TBL_GAMEGOLD_USERS"

-- DROP TABLE "TBL_GAMEGOLD_USERS";

CREATE TABLE "TBL_GAMEGOLD_USERS"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_users'::regclass),
  "MAIN_ACCOUNT_ID" bigint,
  "USER_TYPE" numeric,
  "LOGIN_ACCOUNT" character varying,
  "PASSWORD" character varying,
  "REAL_NAME" character varying,
  "NICK_NAME" character varying,
  "SEX" character varying,
  "QQ" character varying,
  "WEI_XIN" character varying,
  "PHONE_NUMBER" character varying,
  "SIGN" character varying,
  "AVATAR_URL" character varying,
  "LAST_UPDATE_TIME" timestamp without time zone,
  "CREATE_TIME" timestamp without time zone,
  "IS_DELETED" boolean,
  "FUNDS_ACCOUNT" character varying,
  "FUNDS_ACCOUNT_ID" character varying,
  CONSTRAINT "TBL_GAMEGOLD_USERS_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_USERS" 
  OWNER TO postgres;

-- Table: "TBL_GAMEGOLD_LOGS"

-- DROP TABLE "TBL_GAMEGOLD_LOGS";

CREATE TABLE "TBL_GAMEGOLD_LOGS"
(
  "ID" bigint NOT NULL DEFAULT nextval('seq_gamegold_logs'::regclass),
  "CURRENT_THREAD_ID" character varying,
  "CURRENT_USER_ID" bigint,
  "CURRENT_UID" character varying,
  "CURRENT_USER_ACCOUNT" character varying,
  "CURRENT_USER_TYPE" numeric,
  "MODULE" character varying,
  "OPERATE_INFO" character varying,
  "CREATE_TIME" timestamp without time zone,
  CONSTRAINT "TBL_GAMEGOLD_LOGS_pkey" PRIMARY KEY ("ID")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE "TBL_GAMEGOLD_LOGS" 
  OWNER TO postgres;