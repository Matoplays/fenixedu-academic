alter table `CONTRACT_SITUATION` add `HAS_MANDATORY_CREDITS` tinyint(1);
alter table `GRANT_OWNER_EQUIVALENT` add `HAS_MANDATORY_CREDITS` tinyint(1); 
alter table `SERVICE_EXEMPTION` add `HAS_MANDATORY_CREDITS` tinyint(1); 

alter table `CONTRACT_SITUATION` drop COLUMN `MEDICAL_SITUATION`;

alter table `GIAF_PROFESSIONAL_DATA` drop COLUMN `OID_TERMINATION_SITUATION`;
alter table `GIAF_PROFESSIONAL_DATA` drop COLUMN `TERMINATION_SITUATION_GIAF_ID`;