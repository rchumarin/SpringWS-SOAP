CREATE SEQUENCE filename_seq
  MINVALUE 1
  MAXVALUE 9999999999999999999999999999
  START WITH 1
  INCREMENT BY 1
  CACHE 2000;

CREATE TABLE FILENAME (ID NUMBER(*,0) NOT NULL PRIMARY KEY, CODE VARCHAR2(50 BYTE) NOT NULL ENABLE, NUM NUMBER(*,0) NOT NULL ENABLE, FILENAMES VARCHAR2(100 BYTE), ERROR VARCHAR2(100 BYTE));

COMMENT ON COLUMN FILENAME.ID IS 'Идентификатор записи';
COMMENT ON COLUMN FILENAME.CODE IS 'Код выполнения программы';
COMMENT ON COLUMN FILENAME.NUM IS 'Число, переданное на вход';
COMMENT ON COLUMN FILENAME.FILENAMES IS 'Имена файлов, в которых удалось найти число';
COMMENT ON COLUMN FILENAME.ERROR IS 'Описание ошибки, в случае её возникновения';
COMMENT ON TABLE FILENAME  IS 'Таблица содержащая информацию о найденных числах и файлах';

CREATE UNIQUE INDEX IDX_FILENAMES ON FILENAME (FILENAMES);
CREATE UNIQUE INDEX IDX_NUM ON FILENAME (NUM);