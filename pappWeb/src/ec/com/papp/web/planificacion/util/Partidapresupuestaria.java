package ec.com.papp.web.planificacion.util;

import java.util.Collection;

public class Partidapresupuestaria
{
  private Collection<ec.com.papp.planificacion.to.NivelesplanificacionTO> nivelesplanificacionTOs;
  private Collection<ec.com.papp.planificacion.to.ParmnivelesprogramanivelTO> parmnivelesprogramanivelTOs;
  
  public Partidapresupuestaria() {}
  
  public Collection<ec.com.papp.planificacion.to.NivelesplanificacionTO> getNivelesplanificacionTOs() {
    return nivelesplanificacionTOs;
  }
  
  public void setNivelesplanificacionTOs(Collection<ec.com.papp.planificacion.to.NivelesplanificacionTO> nivelesplanificacionTOs) { this.nivelesplanificacionTOs = nivelesplanificacionTOs; }
  
  public Collection<ec.com.papp.planificacion.to.ParmnivelesprogramanivelTO> getParmnivelesprogramanivelTOs() {
    return parmnivelesprogramanivelTOs;
  }
  
  public void setParmnivelesprogramanivelTOs(Collection<ec.com.papp.planificacion.to.ParmnivelesprogramanivelTO> parmnivelesprogramanivelTOs) { this.parmnivelesprogramanivelTOs = parmnivelesprogramanivelTOs; }
}