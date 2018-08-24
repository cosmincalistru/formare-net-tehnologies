INSERT INTO public.employees(
	id_user, first_name, last_name, email, birth_date)
	VALUES ((SELECT id from public.users WHERE username='silviafilip'), 'Filip', 'Silvia', 'silvia@gmail.com', '10-10-2000'),
	((SELECT id from public.users WHERE username='mihaibucurica'), 'Bucurica', 'Mihai', 'mihai@gmail.com', '20-01-1995'),
	((SELECT id from public.users WHERE username='roxanahossu'), 'Hossu', 'Roxana', 'roxana@gmail.com', '20-01-1991'),
	((SELECT id from public.users WHERE username='cosmincalistru'), 'Calistru', 'Cosmin', 'cosmin@gmail.com', '20-01-1991');