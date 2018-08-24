INSERT INTO public.workers(
	date_start, date_end, id_employees, id_projects)
	VALUES ('01-03-2016', '01-02-2017', 
			(SELECT e.id
				FROM public.employees e
			INNER JOIN public.users u
				ON e.id_user = u.id
			WHERE u.username = 'silviafilip'),
			(SELECT id from public.projects WHERE name='Tokheim')),
			
			('01-01-2013', '01-10-2013', 
			(SELECT e.id
				FROM public.employees e
			INNER JOIN public.users u
				ON e.id_user = u.id
			WHERE u.username = 'mihaibucurica'),
			(SELECT id from public.projects WHERE name='Dematerialization')),
			
			('01-02-2017', null, 
			(SELECT e.id
				FROM public.employees e
			INNER JOIN public.users u
				ON e.id_user = u.id
			WHERE u.username = 'mihaibucurica'),
			(SELECT id from public.projects WHERE name='Aflelou')),
			
			('01-09-2017', null, 
			(SELECT e.id
				FROM public.employees e
			INNER JOIN public.users u
				ON e.id_user = u.id
			WHERE u.username = 'roxanahossu'),
			(SELECT id from public.projects WHERE name='Unicorn')),
			
			('01-02-2015', '28-02-2015', 
			(SELECT e.id
				FROM public.employees e
			INNER JOIN public.users u
				ON e.id_user = u.id
			WHERE u.username = 'cosmincalistru'),
			(SELECT id from public.projects WHERE name='Tokheim'));