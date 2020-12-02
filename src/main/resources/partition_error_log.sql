--ERROR LOGS

CREATE SCHEMA IF NOT EXISTS partition_error_log;
--CREATE SEQUENCE IF NOT EXISTS wstb_error_log_seq owned by wstb_error_log.id;

create or replace function insert_partition_error_log()
returns trigger as '
declare
  name text := ''wstb_error_log_'' || NEW.scrape_tour;
  s text;
begin
  -- check that the needed table exists on the database
  perform 1
    from pg_class, pg_namespace
    where relnamespace = pg_namespace.oid
      and relkind = ''r''::"char"
      and relname = name;

  IF NOT FOUND THEN
    BEGIN
      s := ''CREATE TABLE partition_error_log.'' || name || '' (
          CHECK ( scrape_tour = '' || NEW.scrape_tour || '' )
        ) INHERITS ( wstb_error_log )'';   
      EXECUTE s;
    END;
  END IF;
  --NEW.id=nextval(''wstb_error_log_seq'');
  EXECUTE ''INSERT INTO partition_error_log.'' || quote_ident(name) || '' SELECT ($1).*'' using NEW; 
  return null;
end;                                    
' LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS before_insert_on_error_log
  ON public.wstb_error_log;
CREATE TRIGGER before_insert_on_error_log
    BEFORE INSERT ON public.wstb_error_log
    FOR EACH ROW EXECUTE PROCEDURE insert_partition_error_log();