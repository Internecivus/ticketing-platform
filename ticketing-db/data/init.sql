-- password 8t8k5UnbSehiuye

INSERT INTO role(name)
VALUES ('BASIC_FUNCTIONALITY'), ('CATEGORY_ADMINISTRATION'), ('USER_ADMINISTRATION'), ('CATEGORY_USER_ASSIGNMENT'), ('TICKET_ADMINISTRATION');

INSERT INTO user_group(name)
VALUES ('Administrator'), ('Agent'), ('Customer Support'), ('Manager');

INSERT INTO user_group_role(user_group_id, role_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (3, 1), (3, 2), (3, 3), (3, 4), (3, 5), (4, 1), (4, 2), (4, 3), (4, 4), (4, 5);

DO $$
    BEGIN
        ALTER TABLE app_user ALTER COLUMN created_user_id DROP NOT NULL;

        INSERT INTO app_user(username, email)
        VALUES
               ('Internecivus', 'test@test.com'),
               ('Marin Lokas', 'Marin.Lokas@outlook.com'),
               ('Nevenka Radica', 'Nevenka.Radica@outlook.com'),
               ('John Goodman', 'John.Goodman@outlook.com'),
               ('Igor Manas', 'Igor.Manas@outlook.com'),
               ('Edwin Short', 'Edwin.Short@outlook.com'),
               ('Dobrica Lukas', 'Dobrica.Lukas@outlook.com'),
               ('Catherine Hawt', 'Catherine.Hawt@outlook.com'),
               ('Alem Naki', 'Alem.Naki@outlook.com'),
               ('Ivo Vidovan', 'Ivo.Vidovan@outlook.com');


        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('tLd7iXSPe8YAPO8Sx-x8D150WIZQTMMReVVj62PXyyVdqHOZE62yhcfxcv-5BZyD4E1sNV6uhoy8hjk9ndecpyJQFHonnwElzV0Oh0we5idEF4H7JCuNQ0kEuf7frOWDBZNMchdFqWJMMyRTczDKVY9Uzzw2GNPJKf4I50L2UT5m5fTa3tUhnozYFw', 1, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 2, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 3, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 4, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 5, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 6, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 7, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 8, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 9, 'PEPPER_1');
        INSERT INTO credentials(password, user_id, pepper_alias) VALUES ('sygXTioDCN_ruj8C4dcZA2iEmOFbxYbRZxxjBERq1jNGminhmJJzIkRcERuwut5BU39pQIOainQg-NE11AxyIMalUUz5t4s0C2CwJjck-mVqdhXUHfyMh-bqLO2AqdItan9dfWRsLAMDXKfbm8wo3DPMtvx_-msS6OLHXwS3n711aG6Gi_knVbCOuQ', 10, 'PEPPER_1');

        UPDATE app_user SET created_user_id = 1;

        ALTER TABLE app_user ALTER COLUMN created_user_id SET NOT NULL;
    END $$;

INSERT INTO user_user_group(user_id, user_group_id)
SELECT usr.id, ugroup.id
FROM app_user usr
         CROSS JOIN user_group ugroup
WHERE ugroup.name = 'Administrator';

INSERT INTO category(name, created_user_id)
VALUES ('Retail Products', 1), ('Account Management', 1), ('Privacy', 1), ('Customer Feedback', 1), ('Online Shop', 1), ('Careers', 1), ('Internal', 1), ('Returns', 1),('Orders', 1), ('Billing', 1), ('Live Chat', 1), ('Revisions', 1);



INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id, resolved) values
    ('Return of article #21562667', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 0, 1, 8, 1, current_timestamp);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id) values
    ('Registering problem #22', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 0, 5, 2, 1);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id) values
    ('Update of labels on the About Us page', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 0, 3, 7, 1);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id) values
    ('Support for user #671', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 2, 4, 4, 1);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id, resolved) values
    ('Management Revision', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 1, 6, 7, 1, current_timestamp);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id) values
    ('Distributor problem with delivery', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 0, 2, 9, 1);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id) values
    ('Wrong description of article #195132', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 0, 1, 7, 1);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id, resolved) values
    ('Unable to login user #31', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 2, 3, 2, 1, current_timestamp);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id) values
    ('Return of mail items', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 0, 4, 8, 1);

INSERT INTO ticket(title, content, priority, assigned_user_id, category_id, created_user_id) values
    ('Support for AMEX card', 'Some questions that fit into one of the categories listed above may be closed by the community because they aren''t generally a good fit here or need additional information:

Questions seeking debugging help ("why isn''t this code working?") must include the desired behavior, a specific problem or error and the shortest code necessary to reproduce it in the question itself. See: How to create a Minimal, Reproducible Example.

Questions about a problem that can no longer be reproduced or that was caused by a simple typographical error. This can often be avoided by identifying and closely inspecting the shortest program necessary to reproduce the problem before posting.

Questions asking for homework help are not inherently off-topic. However, you must have made a good faith attempt to solve it yourself. The question must include a summary of the work you''ve done so far to solve the problem, and a description of the difficulty you are having solving it. For more detail, see How do I ask and answer homework questions?

Questions asking us to recommend or find a book, tool, software library, tutorial or other off-site resource are off-topic for Stack Overflow as they tend to attract opinionated answers and spam. Instead, describe the problem and what has been done so far to solve it.

Questions asking for customer support with third-party services (such as App Stores) are off-topic for Stack Overflow. Instead, please direct your questions to the relevant company/organisation''s technical support team.

Legal questions, including questions about copyright or licensing, are off-topic for Stack Overflow. Open Source Stack Exchange or Law Stack Exchange may be suitable alternatives.

Questions about general computing hardware and software are off-topic for Stack Overflow unless they directly involve tools used primarily for programming.

Questions on professional server, networking, or related infrastructure administration are off-topic for Stack Overflow unless they directly involve programming or programming tools.', 1, 8, 10, 1);
