-- Таблица с постами
create table if not exists posts(
  id bigserial primary key,
  title varchar(256) not null,
  text CLOB,
  likesCount integer not null,
  tags varchar(1000) not null,
  commentsCount integer not null,
  image BLOB
  );
--таблица с комментами
create table if not exists comments(
  id bigserial primary key,  -- свой id для comments
  postId bigint references posts(id) on delete cascade,
  text varchar(256) not null);
