INSERT INTO worker (firstname, lastname, manager, status, deleted) VALUES
                                                                                 ('John', 'Doe', 'Michael Scott', 'active', false),
                                                                                 ('Jane', 'Smith', 'Jim Halpert', 'active', false),
                                                                                 ('Alice', 'Johnson', 'Pam Beesly', 'inactive', false),
                                                                                 ('Bob', 'Brown', 'Dwight Schrute', 'active', false);

INSERT INTO department (name) VALUES
                                  ('Sales'),
                                  ('Marketing'),
                                  ('IT'),
                                  ('HR');

INSERT INTO worker_department (worker_id, department_id) VALUES
                                                             (1, 1),  -- John Doe in Sales
                                                             (1, 3),  -- John Doe in IT
                                                             (2, 1),  -- Jane Smith in Sales
                                                             (3, 2),  -- Alice Johnson in Marketing
                                                             (4, 1),  -- Bob Brown in Sales
                                                             (4, 4);  -- Bob Brown in HR
