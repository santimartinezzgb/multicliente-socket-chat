# Control de Versión de Java

## Problema Común: Error de Versión de Java

### Síntoma del Error

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.13.0:compile 
(default-compile) on project chat-multiCLI-threads: Fatal error compiling: error: invalid target release: 25
```

Este error ocurre cuando la versión de Java configurada en el `pom.xml` no coincide con la versión de Java instalada en el sistema.

---

## Solución: Ajustar versión en pom.xml a la versión instalada

### 1. Verificar versión de Java instalada

```bash
java -version
# o
mvn --version
```

Salida ejemplo:
```
Java version: 21.0.9, vendor: Ubuntu, runtime: /usr/lib/jvm/java-21-openjdk-amd64
```

### 2. Modificar pom.xml

Localiza la sección del `maven-compiler-plugin` en el archivo `pom.xml`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <source>25</source>  <!-- Cambiar aquí -->
        <target>25</target>  <!-- Cambiar aquí -->
    </configuration>
</plugin>
```

**Cambiar a:**

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <source>21</source>  <!-- Versión correcta -->
        <target>21</target>  <!-- Versión correcta -->
    </configuration>
</plugin>
```

### 3. Limpiar y recompilar

```bash
mvn clean javafx:run
```

---

## Caso Inverso: Actualizar de Java 21 a Java 25

Si tienes Java 21 en el `pom.xml` pero quieres usar Java 25:

### Opción 1: Instalar Java 25 (Recomendado)

#### En Ubuntu/Debian:
```bash
# Agregar repositorio si es necesario
sudo apt update
sudo apt install openjdk-25-jdk

# Verificar instalación
java -version
```

#### Configurar Java por defecto:
```bash
sudo update-alternatives --config java
```

### Opción 2: Downgrade temporal (No recomendado)

Si necesitas mantener Java 21 pero el proyecto requiere features de Java 25, considera:

1. Revisar si realmente necesitas características de Java 25
2. Usar Docker con la versión específica de Java
3. Configurar múltiples versiones de Java con SDKMAN:

```bash
# Instalar SDKMAN
curl -s "https://get.sdkman.io" | bash

# Instalar Java 25
sdk install java 25-open

# Usar Java 25 para este proyecto
sdk use java 25-open
```

### Modificar pom.xml para Java 25:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.13.0</version>
    <configuration>
        <source>25</source>
        <target>25</target>
    </configuration>
</plugin>
```

---

## Regla General

**La versión en `pom.xml` debe ser ≤ a la versión de Java instalada**

- `source`: Versión de sintaxis de Java que usas en el código
- `target`: Versión de bytecode que genera el compilador

**Ejemplo:**
- Java instalado: **21** → pom.xml puede ser **21 o menos**
- Java instalado: **17** → pom.xml puede ser **17 o menos**
- Java instalado: **25** → pom.xml puede ser **25 o menos**

---

## Compatibilidad de Versiones

| Versión Java | Características Principales |
|--------------|----------------------------|
| Java 17 (LTS) | Records, Pattern Matching, Sealed Classes |
| Java 21 (LTS) | Virtual Threads, Sequenced Collections |
| Java 25      | Preview features adicionales |

**LTS** = Long Term Support (soporte a largo plazo)

---

## Troubleshooting Adicional

### Error: "java: invalid source release"
- Verifica que tu IDE esté usando el mismo JDK que Maven
- En IntelliJ: File → Project Structure → Project SDK
- En VS Code: Verifica Java Extension Pack

### Error: "javac: command not found"
- Instalaste JRE en lugar de JDK
- Instala el JDK completo: `sudo apt install openjdk-21-jdk`

### Dependencias de JavaFX
Si cambias de versión de Java, también actualiza las dependencias de JavaFX:

```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.6</version>  <!-- Mantener alineado con Java version -->
</dependency>
```
