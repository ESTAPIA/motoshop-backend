# Pruebas del controlador ImagenProductoController

## Preparación del ambiente
Antes de ejecutar estas pruebas, asegúrese que:
1. La aplicación Spring Boot esté en ejecución en el puerto 9090
2. La base de datos está correctamente configurada y accesible

## Endpoints a probar

### 1. Listar todas las imágenes
**Método**: GET  
**URL**: http://localhost:9090/api/imagenes-producto  
**Comando PowerShell**:
```powershell
Invoke-WebRequest -Uri "http://localhost:9090/api/imagenes-producto" -Method GET -Headers @{"accept"="application/json"} -UseBasicParsing | Select-Object -ExpandProperty Content
```
**Resultado esperado**: 
- Código de estado HTTP 200 (OK)
- Lista JSON de imágenes de productos

### 2. Obtener imagen por ID
**Método**: GET  
**URL**: http://localhost:9090/api/imagenes-producto/{id}  
**Comando PowerShell** (reemplazar {id} con un ID existente):
```powershell
Invoke-WebRequest -Uri "http://localhost:9090/api/imagenes-producto/1" -Method GET -Headers @{"accept"="application/json"} -UseBasicParsing | Select-Object -ExpandProperty Content
```
**Resultado esperado**: 
- Código de estado HTTP 200 (OK)
- Objeto JSON de la imagen encontrada
- Si no existe: Código de estado HTTP 404 (Not Found)

### 3. Obtener imágenes por ID de producto (nuevo endpoint)
**Método**: GET  
**URL**: http://localhost:9090/api/imagenes-producto/producto/{productoId}  
**Comando PowerShell** (reemplazar {productoId} con un ID de producto existente):
```powershell
Invoke-WebRequest -Uri "http://localhost:9090/api/imagenes-producto/producto/1" -Method GET -Headers @{"accept"="application/json"} -UseBasicParsing | Select-Object -ExpandProperty Content
```
**Resultado esperado**: 
- Código de estado HTTP 200 (OK)
- Lista JSON de imágenes asociadas al producto
- Si no hay imágenes: Código de estado HTTP 404 (Not Found)

### 4. Crear nueva imagen
**Método**: POST  
**URL**: http://localhost:9090/api/imagenes-producto  
**Comando PowerShell**:
```powershell
$body = @{
    urlImagen = "https://ejemplo.com/imagen.jpg"
    producto = @{
        id = 1
    }
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:9090/api/imagenes-producto" -Method POST -Headers @{"accept"="application/json"; "Content-Type"="application/json"} -Body $body -UseBasicParsing | Select-Object -ExpandProperty Content
```
**Resultado esperado**: 
- Código de estado HTTP 201 (Created)
- Objeto JSON de la imagen creada con ID asignado

#### Casos de error para probar:
- Enviar datos nulos
- Enviar imagen sin producto
- Enviar imagen sin URL

### 5. Actualizar imagen existente
**Método**: PUT  
**URL**: http://localhost:9090/api/imagenes-producto/{id}  
**Comando PowerShell** (reemplazar {id} con un ID existente):
```powershell
$body = @{
    urlImagen = "https://ejemplo.com/imagen_actualizada.jpg"
    producto = @{
        id = 1
    }
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:9090/api/imagenes-producto/1" -Method PUT -Headers @{"accept"="application/json"; "Content-Type"="application/json"} -Body $body -UseBasicParsing | Select-Object -ExpandProperty Content
```
**Resultado esperado**: 
- Código de estado HTTP 200 (OK)
- Objeto JSON de la imagen actualizada
- Si no existe: Código de estado HTTP 404 (Not Found)

#### Casos de error para probar:
- Intentar actualizar una imagen con ID que no existe
- Enviar datos inválidos (URL vacía, producto nulo, etc.)

### 6. Eliminar imagen
**Método**: DELETE  
**URL**: http://localhost:9090/api/imagenes-producto/{id}  
**Comando PowerShell** (reemplazar {id} con un ID existente):
```powershell
Invoke-WebRequest -Uri "http://localhost:9090/api/imagenes-producto/1" -Method DELETE -Headers @{"accept"="application/json"} -UseBasicParsing | Select-Object -ExpandProperty StatusCode
```
**Resultado esperado**: 
- Código de estado HTTP 204 (No Content)
- Si no existe: Código de estado HTTP 404 (Not Found)

## Conclusiones y mejores prácticas
- Asegúrese de probar tanto los casos de éxito como los casos de error para cada endpoint
- Verifique que las validaciones funcionan correctamente (producto requerido, URL requerida)
- Confirme que la asociación entre productos e imágenes funciona adecuadamente
- Verifique que el nuevo endpoint para obtener imágenes por producto está funcionando correctamente
