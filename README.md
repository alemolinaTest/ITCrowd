# ITCrowd
Weather App Dagger2, ROOM, AndroidNetworking, DataBinding, ViewModel (MVVM), reactivex

Se usa Dagger2 para Dependency Injections

ROOM para presistencia

AndroidNetworking en lugar de Retrofit, solo a modo de prueba. Perimite devolver Observables y es 2 veces mas rapida segun blogs.

DataBinding para evitar asignaciones de views y eventos por codigo

ViewModel con LiveData para evitar perdida de datos en Configuration Changes

LiveData respeta el lifecycle de activities o fragments. LiveData actualiza observers que estan en estado activo.
