CREATE SCHEMA IF NOT EXISTS periodicals;
USE periodicals;

CREATE TABLE IF NOT EXISTS money (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    price_sign VARCHAR(30) NOT NULL,
    coefficient DECIMAL(10,3) UNSIGNED NOT NULL);

CREATE TABLE IF NOT EXISTS language (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    short_name VARCHAR(10) NOT NULL,

    FOREIGN KEY (id)
    REFERENCES money (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS category (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name_en VARCHAR(100) NOT NULL UNIQUE,
    name_ua VARCHAR(100) NOT NULL UNIQUE);

CREATE TABLE IF NOT EXISTS magazine (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    price DECIMAL(10,2) UNSIGNED NOT NULL,
    publication_date DATE NOT NULL,
    image_url VARCHAR(150) NULL DEFAULT NULL,

    FOREIGN KEY (category_id)
    REFERENCES category(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS magazine_localization (
    magazine_id INT NOT NULL,
    language_id INT NOT NULL,
    name VARCHAR(45) NOT NULL UNIQUE,
    description VARCHAR(500) NOT NULL,
    publisher VARCHAR(45) NOT NULL,
    UNIQUE KEY (magazine_id, language_id),

    FOREIGN KEY (magazine_id)
    REFERENCES magazine (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

    FOREIGN KEY (language_id)
    REFERENCES language (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS role (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL DEFAULT 'reader');

CREATE TABLE IF NOT EXISTS user (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(25) NOT NULL UNIQUE,
    password VARCHAR(250) NOT NULL,
    email VARCHAR(30) NOT NULL UNIQUE,
    phone VARCHAR(13) NULL DEFAULT NULL UNIQUE,
    balance DECIMAL(10,2) UNSIGNED NULL DEFAULT '0.00',
    role_id INT NOT NULL DEFAULT 1,

    FOREIGN KEY (role_id)
    REFERENCES role (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS subscription (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    magazine_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    FOREIGN KEY (user_id)
    REFERENCES user (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,

    FOREIGN KEY (magazine_id)
    REFERENCES magazine (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

insert into money values(1, "&#36", "1");
insert into money values(2, "&#8372", "0.05");

insert into language values(1, "EN");
insert into language values(2, "UA");

insert into role values(1, "reader");
insert into role values(2, "blocked_reader");
insert into role values(3, "admin");

insert into user values(1, "login1", "bc547750b92797f955b36112cc9bdd5cddf7d0862151d03a167ada8995aa24a9ad24610b36a68bc02da24141ee51670aea13ed6469099a4453f335cb239db5da", "emaillogin1@gmail.com", "0971111111", 100.00, 1);
insert into user values(2, "login2", "92a891f888e79d1c2e8b82663c0f37cc6d61466c508ec62b8132588afe354712b20bb75429aa20aa3ab7cfcc58836c734306b43efd368080a2250831bf7f363f", "emaillogin2@gmail.com", "0972222222", 00.00, 2);
insert into user values(3, "admin1", "58b5444cf1b6253a4317fe12daff411a78bda0a95279b1d5768ebf5ca60829e78da944e8a9160a0b6d428cb213e813525a72650dac67b88879394ff624da482f", "emailadmin1@gmail.com", "0973333333", 00.00, 3);
insert into user values(4, "admin2", "661bb43140229ad4dc3e762e7bdd68cc14bb9093c158c386bd989fea807acd9bd7f805ca4736b870b6571594d0d8fcfc57b98431143dfb770e083fa9be89bc72", "emailadmin2@gmail.com", "0974444444", 00.00, 3);

insert into category values(1, "Fashion", "Мода");
insert into category values(2, "Beauty", "Краса");
insert into category values(3, "Business", "Бізнес");
insert into category values(4, "Music", "Музика");
insert into category values(5, "Politics", "Політика");

insert into magazine values(1, 1, 5, "2019-11-11", "https://vogue.ua/cache/inline_990x/uploads/article-inline/ce8/1cf/a01/2021_osnovnaya_6177a011cfce8.jpeg");
insert into magazine values(2, 2, 7.50, "2020-10-02", "https://images.ua.prom.st/2660272410_w640_h640_zhurnal-cosmopolitan-10.jpg");
insert into magazine values(3, 1, 10, "2022-02-12", "https://i.mdel.net/i/db/2020/4/1321111/1321111-800w.jpg");
insert into magazine values(4, 3, 10, "2019-01-01", "https://images.squarespace-cdn.com/content/v1/5c341ff75b409b7156f8abdc/1573671758559-RQI71MPT350L0S94B9WL/Forbes+Bri+1.jpg?format=2500w");
insert into magazine values(5, 4, 7.25, "2021-11-20", "https://cdn.shopify.com/s/files/1/2096/4023/products/FD6iWTRXEAgZ5qJ.jpg?v=1636639553");
insert into magazine values(6, 1, 8, "2022-01-13", "https://vanityfair.blob.core.windows.net/vanityfair20210401thumbnails/Covers/0x600/20210401.jpg");
insert into magazine values(7, 5, 9.5, "2022-02-13", "https://d7-invdn-com.investing.com/content/pic95f912935f0cf44cfd25bf2c622d352d.jpg");

insert into magazine_localization values(1, 1, "Vogue", "Vogue is an American monthly fashion and lifestyle magazine that covers many topics, including haute couture fashion, beauty, culture, living, and runway. Based at One World Trade Center in the Financial District of Lower Manhattan, Vogue began as a weekly newspaper in 1892 before becoming a monthly magazine years later. Since starting up in 1892, Vogue has featured numerous actors, musicians, models, athletes, and other prominent celebrities.", "Condé Nast");
insert into magazine_localization values(2, 1, "Cosmopolitan", "Cosmopolitan is an American monthly fashion and entertainment magazine for women, first published based in New York City in March 1886 as a family magazine; it was later transformed into a literary magazine and, since 1965, has become a women's magazine. It was formerly titled The Cosmopolitan. Cosmopolitan magazine is one of the best-selling magazines and is directed mainly towards a female audience. Jessica Pels is the editor-in-chief of Cosmopolitan magazine.", "Hearst Corporation");
insert into magazine_localization values(3, 1, "Harper's Bazaar", "Harper's Bazaar is an American monthly women's fashion magazine. It was first published in New York City on November 2, 1867, as the weekly Harper's Bazar. Harper's Bazaar is published by Hearst and considers itself to be the style resource for 'women who are the first to buy the best, from casual to couture'. Since its debut in 1867, as the U.S.'s first fashion magazine, its pages have been home to talents  of numerous fashion editors, photographers, illustrators and writers.", "Hearst Corporation");
insert into magazine_localization values(4, 1, "Forbes", "Forbes is an American business magazine owned by Integrated Whale Media Investments and the Forbes family. Published eight times a year, it features articles on finance, industry, investing, and marketing topics. Forbes also reports on related subjects such as technology, communications, science, politics, and law. Its headquarters is located in Jersey City, New Jersey. Competitors in the national business magazine category include Fortune and Bloomberg Businessweek.", "Whale Media");
insert into magazine_localization values(5, 1, "Rolling Stone", "Rolling Stone is an American monthly magazine that focuses on music, politics, and popular culture. It was founded in San Francisco, California, in 1967 by Jann Wenner, and the music critic Ralph J. Gleason. It was first known for its coverage of rock music and for political reporting by Hunter S. Thompson. In the 1990s, the magazine broadened and shifted its focus to a younger readership interested in youth-oriented television shows, film actors, and popular music.", "Jann Wenner");
insert into magazine_localization values(6, 1, "Vanity Fair", "Vanity Fair is a monthly magazine of popular culture, fashion, and current affairs published by Condé Nast in the United States. The first version of Vanity Fair was published from 1913 to 1936. The imprint was revived in 1983 and currently includes five international editions of the magazine. As of 2018, the Editor-in-Chief is Radhika Jones.", "Condé Nast");
insert into magazine_localization values(7, 1, "Economist", "The Economist is a British weekly newspaper printed in demitab format and published digitally that focuses on current affairs, international business, politics, technology, and culture. Based in London, the newspaper is owned by The Economist Group, with core editorial offices in the United States, as well as across major cities in continental Europe, Asia, and the Middle East. In 2019, its average global print circulation was over 909,476.", "The Economist Group");

insert into magazine_localization values(1, 2, "Вок", "Vogue — щомісячний жіночий журнал про моду, заснований у 1892 році. Є найвпливовішим модним виданням у світі та «біблією моди». Версії журналу випускаються у двадцяти країнах світу. Vogue став першим журналом в історії модних видань, де на обкладинці була опублікована кольорова фотографія, знімки розміщені на розвороті, зображення обрізані до країв без білих полів, використані постановочні зйомки.", "Конде Наст");
insert into magazine_localization values(2, 2, "Космополітан", "Cosmopolitan — міжнародний щомісячний журнал для жінок. Перший випуск вийшов у 1886 в Сполучених Штатах, але тоді він був сімейним журналом. Згодом він перетворився на літературний журнал, в кінці 1960-х він став жіночим журналом. Скорочена назва Cosmo; видається компанією «Hearst Magazines». Містить інформацію про стосунки, секс, здоров'я, кар'єру, самовдосконалення, знаменитостей, моду та красу. Cosmopolitan має 58 міжнародних випусків і публікується в 110 країнах.", "Харст Корп");
insert into magazine_localization values(3, 2, "Харпер Базаар", "Harper's Bazaar — жіночий журнал про моду, що вперше вийшов у США 1867 року як щотижневик Harper's Bazar. Наразі виходить раз на місяць. Журнал позиціонує себе як гід по стилю для жінок, які хочуть завжди одягатися за останніми модними трендами. Цільова аудиторія – жінки із високим рівнем доходу, середній та вищий клас. З моменту заснування журналу в ньому працюють одні з найкращих фотографів, художників, дизайнерів і журналістів.", "Харст Корп");
insert into magazine_localization values(4, 2, "Форбс", "Форбс — американський діловий журнал, один із найавторитетніших і найвідоміших економічних друкованих видань у світі. Заснований у 1917 році Бертом Форбсом. Журнал відомий завдяки рейтинговим спискам найзаможніших людей світу, пише про історії успіху і поразок підприємців, нові ідеї для бізнесу та інвестицій, публікує авторитетні рейтинги. Forbes має доступ до перших осіб компаній, політиків і лідерів думок і отримує інформацію від найбільш обізнаних джерел.", "Вейл Мідіа");
insert into magazine_localization values(5, 2, "Роллінґ Стоун", "Rolling Stone — американський журнал, присвячений музиці й поп-культурі. Виходить двічі на місяць. Наклад — приблизно півтора мільйони примірників. Випуски Rolling Stone, як правило, містять музичні та кіно рецензії, історії й фотографії знаменитостей, інформацію про нових артистів, поради щодо моди і статті про політику. Сьогодні журнал має кілька міжнародних версій.", "Янн Веннер");
insert into magazine_localization values(6, 2, "Венеті Фейр", "Ве́неті Фейр або Ва́ниті Фейр — американський журнал, присвячений політиці, моді та іншим аспектам масової культури. Видається компанією «Конде Наст» (Condé Nast Publications).", "Конде Наст");
insert into magazine_localization values(7, 2, "Економіст", "Економіст — впливовий щотижневий англомовний журнал. Публікується в Англії з 1843 року. 2006 тираж перевищив мільйон екземплярів, більше половини яких була продана в Північній Америці. Через глобальну орієнтацію, «Економіст» не вважається виключно британським виданням. Традиційно видання вважає себе газетою і не підписує публікації. Основні теми, що освітлюються журналом, — політичні події, міжнародні відносини, фінансові, економічні і ділові новини, а також наука і культура.", "Економіст Груп");

insert into subscription values(1, 1, 1, "2021-10-10", "2021-11-10");
insert into subscription values(2, 1, 2, "2021-12-24", "2022-01-24");
insert into subscription values(3, 2, 1, "2022-01-05", "2022-02-05");
insert into subscription values(4, 1, 3, "2021-03-10", "2021-04-10");
insert into subscription values(5, 2, 2, "2022-02-12", "202-03-12");
insert into subscription values(6, 2, 5, "2022-03-07", "2022-04-07");