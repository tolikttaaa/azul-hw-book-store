# API
## /auth
**Authority: *Permit ALL***

1. **[POST]** /auth/signup

   Register a new `User` with `USER` role

2. **[POST]** /auth/login

   Get token for authentication


## /book
**Authority: *Permit USER, ADMIN***

1. **[GET]** /book/search

   Get paginated list of `Books` by params:
   * Book's title
   * Book author's first name
   * Book author's last name
   * Book author's middle name
   * Book's genre


## /data/user
**Authority: *Permit ADMIN***

1. **[GET]** /data/user/

   Get list of all `Users`

2. **[PUT]** /data/user/role/_{username}_

   Update `User`'s role by `username`

3. **[DELETE]** /data/user/_{username}_

   Delete `User` by `username`


## /data/book
**Authority: *Permit ADMIN***

1. **[GET]** /data/book/

   Get list of all `Books`

2. **[POST]** /data/book/new

   Create new `Book`

3. **[GET]** /data/book/_{id}_

   Get `Book` by `id`

4. **[PUT]** /data/book/_{id}_

   Update `Book` by `id`

5. **[PUT]** /data/book/_{id}_/title

   Update `Book`'s title by `id`

6. **[PUT]** /data/book/_{id}_/price

   Update `Book`'s price by `id`

7. **[PUT]** /data/book/_{id}_/author

   Update `Book`'s author by `id`

8. **[PUT]** /data/book/_{id}_/genres

   Update `Book`'s genres by `id`

9. **[DELETE]** /data/book/_{id}_

   Delete `Book` by `id`


## /data/genre
**Authority: *Permit ADMIN***

1. **[GET]** /data/genre/

   Get list of all `Genres`

2. **[POST]** /data/genre/new

   Create new `Genre`

3. **[GET]** /data/genre/_{id}_

   Get `Genre` by `id`

4. **[PUT]** /data/genre/_{id}_

   Update `Genre` by `id`

5. **[DELETE]** /data/genre/_{id}_

   Delete `Genre` by `id`


## /data/author
**Authority: *Permit ADMIN***

1. **[GET]** /data/author/

   Get list of all `Authors`

2. **[POST]** /data/author/new

   Create new `Author`

3. **[GET]** /data/author/_{id}_

   Get `Author` by `id`

4. **[PUT]** /data/author/_{id}_

   Update `Author` by `id`

5. **[DELETE]** /data/author/_{id}_

   Delete `Author` by `id`
