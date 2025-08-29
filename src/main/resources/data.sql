-- Insert default roles
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_MANAGER');
INSERT IGNORE INTO roles (id, name) VALUES (3, 'ROLE_EMPLOYEE');

-- Insert users with their role_id
-- INSERT IGNORE INTO users (id, username, password, role_id, manager_id) VALUES (1, 'admin', 'admin123', 1, 1);
-- INSERT IGNORE INTO users (id, username, password, role_id, manager_id) VALUES (2, 'manager', 'manager123', 2, 1);
-- INSERT IGNORE INTO users (id, username, password, role_id, manager_id) VALUES (3, 'employee', 'emp123', 3, 2);

INSERT IGNORE INTO departments (id, name, description) VALUES (1, 'HR', 'Human Resources Department');
INSERT IGNORE INTO departments (id, name, description) VALUES (2, 'IT', 'Information Technology Department');
-- INSERT IGNORE INTO departments (id, name, description) VALUES (3, 'Finance', 'Finance Department');
