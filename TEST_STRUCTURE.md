# Estructura de Tests del Proyecto CaPr3

## 📁 Estructura de Carpetas

```
src/test/java/com/casopractico3/CaPr3/
├── repository/          # Tests de Repositorio (@DataJpaTest)
├── unit/               # Tests Unitarios de Servicios (@ExtendWith + Mockito)
├── controller/         # Tests de Controladores (@WebMvcTest)
└── integration/        # Tests de Integración (@SpringBootTest)
```

## 📋 Tests Generados

### 1️⃣ TESTS UNITARIOS (carpeta: `unit`)
**Herramientas:** JUnit 5 + Mockito | **Anotación:** @ExtendWith(MockitoExtension.class)

- **ClientServiceTest.java**
  - `shouldCreateClientSuccessfully()` - Crear cliente exitosamente
  - `shouldThrowExceptionWhenClientDniAlreadyExists()` - DNI duplicado
  - `shouldThrowExceptionWhenClientEmailAlreadyExists()` - Email duplicado
  - `shouldThrowExceptionWhenClientPhoneAlreadyExists()` - Teléfono duplicado
  - `shouldThrowExceptionWhenFirstNameIsBlank()` - Validación de nombre
  - `shouldListAllClientsSuccessfully()` - Listar todos los clientes
  - `shouldGetClientByIdSuccessfully()` - Obtener cliente por ID
  - `shouldThrowExceptionWhenClientNotFoundById()` - Cliente no encontrado
  - `shouldGetClientByDniSuccessfully()` - Obtener cliente por DNI
  - `shouldThrowExceptionWhenClientNotFoundByDni()` - DNI no encontrado

- **AccountServiceTest.java**
  - `shouldCreateAccountSuccessfully()` - Crear cuenta exitosamente
  - `shouldThrowExceptionWhenClientNotFoundForCreate()` - Cliente no existe
  - `shouldListClientAccountsSuccessfully()` - Listar cuentas del cliente
  - `shouldThrowExceptionWhenClientNotFoundForList()` - Cliente no existe
  - `shouldGetAccountByIbanSuccessfully()` - Obtener cuenta por IBAN
  - `shouldThrowExceptionWhenAccountNotFoundByIban()` - IBAN no encontrado

- **OperationServiceTest.java**
  - `shouldDepositSuccessfully()` - Depósito exitoso
  - `shouldThrowExceptionWhenDepositAmountIsZero()` - Monto cero
  - `shouldThrowExceptionWhenDepositAmountIsNegative()` - Monto negativo
  - `shouldThrowExceptionWhenAccountNotFoundForDeposit()` - Cuenta no existe
  - `shouldWithdrawSuccessfully()` - Retiro exitoso
  - `shouldThrowExceptionWhenWithdrawAmountIsZero()` - Monto cero
  - `shouldThrowExceptionWhenAccountNotFoundForWithdraw()` - Cuenta no existe
  - `shouldTransferSuccessfully()` - Transferencia exitosa
  - `shouldThrowExceptionWhenTransferSameAccount()` - Transferencia a mismo IBAN
  - `shouldThrowExceptionWhenSourceAccountNotFound()` - Cuenta origen no existe
  - `shouldThrowExceptionWhenInsufficientBalance()` - Saldo insuficiente

- **MovementServiceTest.java**
  - `shouldSaveMovementSuccessfully()` - Guardar movimiento
  - `shouldFindMovementByIdSuccessfully()` - Obtener movimiento por ID
  - `shouldReturnEmptyWhenMovementNotFound()` - Movimiento no encontrado
  - `shouldFindAllMovementsSuccessfully()` - Listar todos los movimientos
  - `shouldDeleteMovementSuccessfully()` - Eliminar movimiento

### 2️⃣ TESTS DE REPOSITORIO (carpeta: `repository`)
**Herramientas:** @DataJpaTest | **BD:** H2 en memoria

- **ClientRepositoryTest.java**
  - `shouldSaveClientSuccessfully()` - Guardar cliente
  - `shouldFindClientByIdSuccessfully()` - Buscar por ID
  - `shouldReturnEmptyWhenClientNotFound()` - No encontrado
  - `shouldFindAllClientsSuccessfully()` - Listar todos
  - `shouldDeleteClientSuccessfully()` - Eliminar
  - `shouldFindClientByDniSuccessfully()` - Buscar por DNI
  - `shouldReturnEmptyWhenClientNotFoundByDni()` - DNI no encontrado
  - `shouldReturnTrueWhenClientExistsByDni()` - Existe por DNI
  - `shouldReturnFalseWhenClientNotExistsByDni()` - No existe por DNI
  - `shouldReturnTrueWhenClientExistsByEmail()` - Existe por Email
  - `shouldReturnFalseWhenClientNotExistsByEmail()` - No existe por Email
  - `shouldReturnTrueWhenClientExistsByPhone()` - Existe por Teléfono
  - `shouldReturnFalseWhenClientNotExistsByPhone()` - No existe por Teléfono
  - `shouldUpdateClientSuccessfully()` - Actualizar cliente

- **AccountRepositoryTest.java**
  - `shouldSaveAccountSuccessfully()` - Guardar cuenta
  - `shouldFindAccountByIdSuccessfully()` - Buscar por ID
  - `shouldReturnEmptyWhenAccountNotFound()` - No encontrado
  - `shouldFindAllAccountsSuccessfully()` - Listar todas
  - `shouldDeleteAccountSuccessfully()` - Eliminar
  - `shouldFindAccountByIbanSuccessfully()` - Buscar por IBAN
  - `shouldReturnEmptyWhenAccountNotFoundByIban()` - IBAN no encontrado
  - `shouldFindAccountsByClientIdSuccessfully()` - Listar por cliente
  - `shouldReturnEmptyListWhenClientHasNoAccounts()` - Sin cuentas
  - `shouldUpdateAccountSuccessfully()` - Actualizar cuenta
  - `shouldHandleAccountWithNullBalance()` - Balance nulo

- **MovementRepositoryTest.java**
  - `shouldSaveMovementSuccessfully()` - Guardar movimiento
  - `shouldFindMovementByIdSuccessfully()` - Buscar por ID
  - `shouldReturnEmptyWhenMovementNotFound()` - No encontrado
  - `shouldFindAllMovementsSuccessfully()` - Listar todos
  - `shouldDeleteMovementSuccessfully()` - Eliminar
  - `shouldHandleMultipleMovementsForSameAccount()` - Múltiples movimientos
  - `shouldPersistMovementTimestamp()` - Persistencia de timestamp

### 3️⃣ TESTS DE CONTROLADOR (carpeta: `controller`)
**Herramientas:** @WebMvcTest + MockMvc | **Mocks:** Servicios

- **ClientControllerTest.java**
  - `shouldCreateClientSuccessfully()` - POST /clients (201)
  - `shouldGetClientByIdSuccessfully()` - GET /clients/getById/{id} (200)
  - `shouldReturn404WhenClientNotFoundById()` - GET /clients/getById/999 (404)
  - `shouldGetClientByDniSuccessfully()` - GET /clients/getByDni/{dni} (200)
  - `shouldReturn404WhenClientNotFoundByDni()` - GET /clients/getByDni/invalid (404)
  - `shouldGetAllClientsSuccessfully()` - GET /clients/getAll (200)
  - `shouldReturnEmptyListWhenNoClients()` - GET /clients/getAll (200 vacío)
  - `shouldValidateClientCreationRequestBody()` - Validación de entrada (400)

- **AccountControllerTest.java**
  - `shouldCreateAccountSuccessfully()` - POST /accounts/create/{clientId} (200)
  - `shouldReturn404WhenClientNotFoundForCreate()` - POST con cliente no encontrado (404)
  - `shouldListClientAccountsSuccessfully()` - GET /accounts/client/{clientId} (200)
  - `shouldReturn404WhenClientNotFoundForList()` - GET con cliente no encontrado (404)
  - `shouldReturnEmptyListWhenClientHasNoAccounts()` - GET /accounts/client/{clientId} (200 vacío)
  - `shouldGetAccountByIbanSuccessfully()` - GET /accounts/iban/{iban} (200)
  - `shouldReturn404WhenAccountNotFoundByIban()` - GET /accounts/iban/invalid (404)
  - `shouldConvertIbanToUpperCase()` - Conversión de IBAN a mayúsculas

- **OperationControllerTest.java**
  - `shouldDepositSuccessfully()` - POST /operations/deposit (200)
  - `shouldReturn404WhenAccountNotFoundForDeposit()` - POST depósito sin cuenta (404)
  - `shouldWithdrawSuccessfully()` - POST /operations/withdraw (200)
  - `shouldReturn404WhenAccountNotFoundForWithdraw()` - POST retiro sin cuenta (404)
  - `shouldTransferSuccessfully()` - POST /operations/transfer (200)
  - `shouldReturn404WhenSourceAccountNotFoundForTransfer()` - POST transferencia sin origen (404)
  - `shouldReturnBadRequestWhenInvalidAmount()` - Monto inválido (400)

### 4️⃣ TESTS DE INTEGRACIÓN (carpeta: `integration`)
**Herramientas:** @SpringBootTest + TestRestTemplate | **BD:** H2

- **ClientIntegrationTest.java**
  - `shouldCreateClientEndToEnd()` - Flujo completo crear cliente
  - `shouldGetClientByIdEndToEnd()` - Flujo completo buscar por ID
  - `shouldReturnNotFoundWhenClientIdDoesNotExist()` - Cliente no existe
  - `shouldGetClientByDniEndToEnd()` - Flujo completo buscar por DNI
  - `shouldReturnNotFoundWhenClientDniDoesNotExist()` - DNI no existe
  - `shouldListAllClientsEndToEnd()` - Flujo completo listar
  - `shouldReturnEmptyListWhenNoClients()` - Lista vacía
  - `shouldRejectDuplicateDni()` - Rechazo DNI duplicado
  - `shouldRejectDuplicateEmail()` - Rechazo Email duplicado
  - `shouldRejectDuplicatePhone()` - Rechazo Teléfono duplicado
  - `shouldPersistClientDataSuccessfully()` - Verificar persistencia

- **AccountIntegrationTest.java**
  - `shouldCreateAccountEndToEnd()` - Flujo completo crear cuenta
  - `shouldReturnNotFoundWhenClientDoesNotExist()` - Cliente no existe
  - `shouldGetAccountByIbanEndToEnd()` - Flujo completo buscar por IBAN
  - `shouldReturnNotFoundWhenIbanDoesNotExist()` - IBAN no existe
  - `shouldListClientAccountsEndToEnd()` - Flujo completo listar cuentas
  - `shouldReturnEmptyListWhenClientHasNoAccounts()` - Sin cuentas
  - `shouldReturnNotFoundWhenClientHasNoAccountsAndClientDoesNotExist()` - Cliente no existe
  - `shouldConvertIbanToUpperCaseEndToEnd()` - Conversión IBAN
  - `shouldPersistAccountDataSuccessfully()` - Verificar persistencia
  - `shouldCreateMultipleAccountsForSameClient()` - Múltiples cuentas

- **OperationIntegrationTest.java**
  - `shouldDepositEndToEnd()` - Flujo completo depósito
  - `shouldWithdrawEndToEnd()` - Flujo completo retiro
  - `shouldReturnNotFoundWhenAccountNotFoundForDeposit()` - Cuenta no existe
  - `shouldReturnNotFoundWhenAccountNotFoundForWithdraw()` - Cuenta no existe
  - `shouldReturnBadRequestWhenInsufficientBalance()` - Saldo insuficiente
  - `shouldTransferEndToEnd()` - Flujo completo transferencia
  - `shouldReturnBadRequestWhenTransferSameAccount()` - Mismo IBAN
  - `shouldReturnNotFoundWhenSourceAccountNotFoundForTransfer()` - Origen no existe
  - `shouldReturnNotFoundWhenTargetAccountNotFoundForTransfer()` - Destino no existe
  - `shouldPersistMovementsSuccessfully()` - Verificar persistencia de movimientos
  - `shouldHandleMultipleOperations()` - Múltiples operaciones

## 📊 Resumen de Tests

| Tipo | Archivos | Tests |
|------|----------|-------|
| Unitarios | 4 | 38 |
| Repositorio | 3 | 34 |
| Controlador | 3 | 25 |
| Integración | 3 | 34 |
| **TOTAL** | **13** | **131** |

## ✅ Cobertura

- ✓ Crear (POST)
- ✓ Leer (GET by ID, GET by DNI, GET all)
- ✓ Actualizar (PUT)
- ✓ Eliminar (DELETE)
- ✓ Casos de error (404, 400)
- ✓ Validaciones
- ✓ Excepciones personalizadas
- ✓ Persistencia real (BD H2)
- ✓ Flujos end-to-end completos
- ✓ Operaciones complejas (transferencias)
- ✓ Múltiples registros

## 🚀 Cómo Ejecutar

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar solo unitarios
mvn test -Dtest=*ServiceTest

# Ejecutar solo de repositorio
mvn test -Dtest=*RepositoryTest

# Ejecutar solo de controlador
mvn test -Dtest=*ControllerTest

# Ejecutar solo integración
mvn test -Dtest=*IntegrationTest

# Con cobertura
mvn test jacoco:report
```

## 📝 Características

✅ JUnit 5 + Mockito + Spring Test
✅ Mocking completo de dependencias
✅ Base de datos H2 en memoria
✅ TestRestTemplate para integración
✅ MockMvc para tests de controladores
✅ TestEntityManager para persistencia
✅ Given-When-Then pattern en unitarios
✅ Verify() para aserciones de interacciones
✅ Cobertura completa de servicios, repositorios y controladores
✅ Tests de casos excepcionales y validaciones
