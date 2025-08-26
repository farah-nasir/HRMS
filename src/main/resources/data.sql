-- Insert default roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_MANAGER');
INSERT INTO roles (id, name) VALUES (3, 'ROLE_EMPLOYEE');

-- Insert users with their role_id
INSERT INTO users (id, username, password, role_id) VALUES (1, 'admin', 'admin123', 1);
INSERT INTO users (id, username, password, role_id) VALUES (2, 'manager', 'manager123', 2);
INSERT INTO users (id, username, password, role_id) VALUES (3, 'employee', 'emp123', 3);
