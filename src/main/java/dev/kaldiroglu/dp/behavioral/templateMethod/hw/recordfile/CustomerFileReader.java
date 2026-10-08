package dev.kaldiroglu.dp.behavioral.templateMethod.hw.recordfile;

/** Reads lines like {@code Ayse;Istanbul}. A line without ';' is an error. */
public final class CustomerFileReader extends RecordFileReader<Customer> {

    @Override
    protected Customer parse(String line) {
        String[] parts = line.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("not a customer: " + line);
        }
        return new Customer(parts[0].trim(), parts[1].trim());
    }
}
