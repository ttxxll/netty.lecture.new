namespace java thrift

typedef i16 short
typedef i32 int
typedef i64 long
typedef bool boolean
typedef string String

struct Person {
    1: optional String username,
    2: optional int age,
    3: optional boolean married
}

exception DateException {
    1: optional String message,
    2: optional String callStack,
    3: optional String date
}

service PersonService {

    Person getPersonByUsername(1: required String username) throws (1: DateException dataException),

    void savePerson(1: required Person person) throws (1: DateException dataException)
}