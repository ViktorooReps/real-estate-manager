# Описание структуры базы данных

![Диаграмма БД](database_diagram.jpeg)

* `Client` Таблица клиентов агентства.
* `Order` Таблица заказов. Все значения либо принимают (возможно пустые) массивы или json-объекты, либо имеют возможность принимать `NULL` значение. Исключение - `order_id`, `ordered_by` и `contract_type` поля.
* `Offer` Таблица предложений. Все значения фиксированы, `NULL` значений нет.

## Описание json полей:

### Order
* `estate_types` - имеет вид отношения `"тип недвижимости": bool`, задающее допустимые типы недвижимости (комната, квартира, дом и пр.).
* `estate_facades` - имеет вид отношения `"тип фасада": bool`, задающее допустимые типы фасадов (деревянный, панельный, кирпичный и пр.).
* `space_min` - имеет вид отношения `"комната": "минимальная площадь"`, задающее минимальные требования к площади в квадратных метрах отдельных комнатах (и недвижимости в целом).
* `commodities` - имеет вид отношения `"удобства": bool`, задающее требования по наличие удобств (раздельный санузел, Интернет, телевидение и пр.).
* `transport_max` - имеет вид отношения `"тип транспорта": "максимальное расстояние"`, задающее максимально допустимое расстояние до каких-либо видов транспорта в минутах пешком (метро, электричка, автобус и пр.).
* `locations` - имеет вид отношения `"расположение": bool`, задающее допустимые расположения недвижимости (города Московской области, Москва внутри МКАД, Москва вне МКАД и пр.).

### Offer
* `space` - имеет вид отношения `"комната": "площадь"`, задающее площадь в квадратных метрах отдельных комнатах (и недвижимости в целом).
* `commodities` - имеет вид отношения `"удобства": bool`, задающее наличие удобств (раздельный санузел, Интернет, телевидение и пр.).
* `transport` - имеет вид отношения `"тип транспорта": "расстояние"`, задающее расстояние до каких-либо видов транспорта в минутах пешком (метро, электричка, автобус и пр.).