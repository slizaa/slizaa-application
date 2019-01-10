package org.slizaa.server.service.slizaa.internal.structuredatabase;

import org.slizaa.server.service.slizaa.IStructureDatabase;

public interface IInternalStructureDatabase extends IStructureDatabase {

  void _start();

  void _stop();

  void _parse();
}
