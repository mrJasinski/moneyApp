alter table TRANSACTIONS drop column day;
alter table TRANSACTIONS drop column month;
alter table TRANSACTIONS drop column year;
alter table TRANSACTIONS drop column IS_PAID_ID;
alter table TRANSACTIONS drop column account_id;
alter table TRANSACTIONS drop column BUDGET_ID;
alter table TRANSACTIONS add column bill_id int;
alter table TRANSACTIONS add foreign key (bill_id) references bills(id);

