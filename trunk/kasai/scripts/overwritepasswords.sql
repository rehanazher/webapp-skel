-- Sample commands to overwrite the passwords in Kasai 2.0 when using the RDBMSAuthService

-- Use one of the following to overwrite the passwords in the Kasai database when migrating from 1.x to 2.0
-- ALL PASSWORDS WILL BE CHANGED TO "changeme", you have to instruct all users to change their passwords ASAP.

-- Use the following if you will use "strong" password hashing (see Config.properties)
update kasai_users set password='eGerLs/YTk331qetZW+npcbGQwvwIftXVpEOkUuoxkzDeCiLaLhyL+C698+03GqN'

-- Use the following if you will use "basic" password hashing (see Config.properties)
update kasai_users set password='6+SRgGHsjJijPJ8R6MzJDX6klvwNJ2Zh'

-- Use the following if you will use "cleartext" password hashing (see Config.properties)
update kasai_users set password='changeme'