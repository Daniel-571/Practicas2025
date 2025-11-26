package Practicas;

import java.io.*; 
import okhttp3.*; 
import com.google.gson.*; 
import org.apache.poi.xssf.usermodel.*; 

public class ConsumoCalefaccion{

    public static void main(String[] args) {
        //Url de la api Open-Meteo con coordenadas de Madrid
        String url = "https://api.open-meteo.com/v1/forecast?latitude=40.4168&longitude=-3.7038&daily=temperature_2m_max,temperature_2m_min&timezone=Europe/Madrid";

        //Cliente HTTP para hacer la petición
        OkHttpClient cliente = new OkHttpClient();

        try {
            //Creamos la petición con la url, aqui no se envia todavia
            Request req = new Request.Builder().url(url).build();

            //Ejecutamos la petición y obtenemos la respuesta
            Response res = cliente.newCall(req).execute();

            //Si la respuesta no es correcta, mostramos el error y salimos
            if (!res.isSuccessful()) {
                System.out.println("Error: " + res.code());
                return;
            }

            //Leemos el cuerpo de la respuesta como texto JSON
            String cuerpo = res.body().string();

            //Lo convertimos en un objeto JSON
            JsonObject raiz = JsonParser.parseString(cuerpo).getAsJsonObject();

            //Accedemos al bloque "daily" que contiene los datos por día
            JsonObject datos = raiz.getAsJsonObject("daily");

            //Extraemos los arrays de fechas, temperaturas máximas y mínimas
            JsonArray fechas = datos.getAsJsonArray("time");
            JsonArray max = datos.getAsJsonArray("temperature_2m_max");
            JsonArray min = datos.getAsJsonArray("temperature_2m_min");

            //Creamos el libro Excel
            XSSFWorkbook libro = new XSSFWorkbook();

            //Creamos una hoja llamada "Calefacción"
            var hoja = libro.createSheet("Calefacción");

            //Creamos la primera fila con los títulos
            var cabecera = hoja.createRow(0);
            cabecera.createCell(0).setCellValue("Fecha");
            cabecera.createCell(1).setCellValue("Temp Media");
            cabecera.createCell(2).setCellValue("Consumo");

            //Recorremos los 7 primeros días
            for (int i = 0; i < 7; i++) {
                // Obtenemos la fecha
                String fecha = fechas.get(i).getAsString();

                //Calculamos la temperatura media  (max+min)/2
                double tMedia = (max.get(i).getAsDouble() + min.get(i).getAsDouble()) / 2;

                //Calculamos el consumo: si la temperatura media es menor que 15, hay consumo
                double consumo;
                
                if (tMedia < 15) {
                    consumo = 15 - tMedia;
                } else {
                    consumo = 0;
                }

                //Creamos una nueva fila en el Excel
                var fila = hoja.createRow(i+1);
                fila.createCell(0).setCellValue(fecha);
                fila.createCell(1).setCellValue(tMedia);
                fila.createCell(2).setCellValue(consumo);
            }

            //Ruta donde se guardará el archivo
            String ruta = "C:\\Users\\Daniel\\Documents\\calefaccion\\consumo.xlsx";

            //Creamos la carpeta si no existe
            new File("C:\\Users\\Daniel\\Documents\\calefaccion").mkdirs();

            //Guardamos el archivo Excel en la ruta indicada
            try (FileOutputStream out = new FileOutputStream(ruta)) {
                libro.write(out);
                System.out.println("Excel guardado en: "+ruta);
            }

        } catch (Exception e) {
            //Si ocurre cualquier error, lo mostramos
            System.out.println("Error: " + e.getMessage());
        }
    }
}